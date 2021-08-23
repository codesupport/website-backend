package dev.codesupport.web.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.codesupport.testutils.builders.UserBuilder;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.common.security.hashing.HashingUtility;
import dev.codesupport.web.common.security.jwt.JwtUtility;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.domain.TokenResponse;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserProfileStripped;
import dev.codesupport.web.domain.UserRegistration;
import dev.codesupport.web.domain.UserStripped;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class UserServiceImplTest {

    private static ObjectMapper mapper;

    private static UserServiceImpl service;

    private static HashingUtility mockHashingUtility;

    private static CrudOperations<UserEntity, User, Long> mockUserCrudOperations;
    private static CrudOperations<UserEntity, UserProfileStripped, Long> mockUserProfileCrudOperations;

    private static UserRepository mockUserRepository;

    private static List<User> getUserList;

    private static JwtUtility mockJwtUtility;

    @BeforeClass
    public static void init() {
        mapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        //unchecked - this is fine for a unit test
        //noinspection unchecked
        mockUserCrudOperations = mock(CrudOperations.class);
        //unchecked - this is fine for a unit test
        //noinspection unchecked
        mockUserProfileCrudOperations = mock(CrudOperations.class);

        mockUserRepository = mock(UserRepository.class);
        mockJwtUtility = mock(JwtUtility.class);

        ApplicationContext mockContext = mock(ApplicationContext.class);

        ReflectionTestUtils.setField(mockUserCrudOperations, "context", mockContext);
        ReflectionTestUtils.setField(mockUserProfileCrudOperations, "context", mockContext);

        mockHashingUtility = mock(HashingUtility.class);

        service = new UserServiceImpl(mockUserRepository, mockHashingUtility, mockJwtUtility);
        ReflectionTestUtils.setField(service, "userCrudOperations", mockUserCrudOperations);
        ReflectionTestUtils.setField(service, "userProfileCrudOperations", mockUserProfileCrudOperations);

        List<UserBuilder> userBuilders = Collections.singletonList(
                UserBuilder.builder()
                        .id(5L)
                        .alias("timmy")
                        .hashPassword("1234567890abcdef")
                        .email("valid@email.com")
                        .avatarLink("timmeh.jpg")
                        .joinDate(1L)
        );

        getUserList = userBuilders.stream().map(UserBuilder::buildDomain).collect(Collectors.toList());
    }

    @Before
    public void setUp() {
        Mockito.reset(
                mockUserCrudOperations,
                mockUserProfileCrudOperations,
                mockUserRepository,
                mockHashingUtility,
                mockJwtUtility
        );
    }

    private ObjectMapper mapper() {
        return mapper;
    }

    @Test
    public void shouldReturnCorrectUsersWithFindAllUserProfiles() {
        List<UserProfileStripped> expected = mapper()
                .convertValue(getUserList, new TypeReference<List<UserProfileStripped>>() {
                });

        doReturn(expected)
                .when(mockUserProfileCrudOperations)
                .getAll();

        List<UserProfileStripped> actual = service.findAllUserProfiles();

        assertEquals(expected, actual);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionWithGetUserProfileByAliasIfAliasDoesNotExist() {
        String alias = "username";

        Optional<UserEntity> optional = Optional.empty();

        doReturn(optional)
                .when(mockUserRepository)
                .findByAliasIgnoreCase(alias);

        service.getUserProfileByAlias(alias);
    }

    @Test
    public void shouldReturnCorrectUsersWithGetUserProfileByAlias() {
        String alias = "username";

        UserProfile expected = mapper()
                .convertValue(getUserList.get(0), UserProfile.class);

        UserEntity userEntity = mapper()
                .convertValue(getUserList.get(0), UserEntity.class);

        Optional<UserEntity> optional = Optional.of(userEntity);

        doReturn(optional)
                .when(mockUserRepository)
                .findByAliasIgnoreCase(alias);

        UserProfile actual = service.getUserProfileByAlias(alias);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectUsersWithGetUserProfileById() {
        Long id = 1L;

        List<UserProfileStripped> returnedUsers = mapper()
                .convertValue(getUserList, new TypeReference<List<UserProfileStripped>>() {
                });

        doReturn(returnedUsers.get(0))
                .when(mockUserProfileCrudOperations)
                .getById(id);

        UserProfileStripped expected = returnedUsers.get(0);
        UserProfileStripped actual = service.getUserProfileById(id);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectUsersWithFindAllUsers() {
        List<UserStripped> expected = mapper()
                .convertValue(getUserList, new TypeReference<List<UserStripped>>() {
                });

        doReturn(getUserList)
                .when(mockUserCrudOperations)
                .getAll();

        List<UserStripped> actual = service.findAllUsers();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectUsersWithGetUserById() {
        Long id = 1L;

        List<UserStripped> returnedUsers = mapper()
                .convertValue(getUserList, new TypeReference<List<UserStripped>>() {
                });

        doReturn(getUserList.get(0))
                .when(mockUserCrudOperations)
                .getById(id);

        UserStripped expected = returnedUsers.get(0);
        UserStripped actual = service.getUserById(id);

        assertEquals(expected, actual);
    }

    @Test(expected = ServiceLayerException.class)
    public void shouldThrowServiceLayerExceptionIfRegisteringWithNonUniqueAlias() {
        String token = "tokentokentokentoken";

        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setAlias("timmy");
        userRegistration.setPassword("1234567890abcdef");
        userRegistration.setEmail("valid@email.com");

        User user = mapper().convertValue(userRegistration, User.class);

        doReturn(true)
                .when(mockUserRepository)
                .existsByAliasIgnoreCase(userRegistration.getAlias());

        doReturn(false)
                .when(mockUserRepository)
                .existsByEmailIgnoreCase(userRegistration.getEmail());

        doReturn(getUserList.get(0))
                .when(mockUserCrudOperations)
                .createEntity(user);

        doReturn(token)
                .when(mockJwtUtility)
                .generateToken(getUserList.get(0).getAlias(), getUserList.get(0).getEmail());

        service.registerUser(userRegistration);
    }

    @Test(expected = ServiceLayerException.class)
    public void shouldThrowServiceLayerExceptionIfRegisterWithNonUniqueEmail() {
        String token = "tokentokentokentoken";

        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setAlias("timmy");
        userRegistration.setPassword("1234567890abcdef");
        userRegistration.setEmail("valid@email.com");

        User user = mapper().convertValue(userRegistration, User.class);

        doReturn(false)
                .when(mockUserRepository)
                .existsByAliasIgnoreCase(userRegistration.getAlias());

        doReturn(true)
                .when(mockUserRepository)
                .existsByEmailIgnoreCase(userRegistration.getEmail());

        doReturn(getUserList.get(0))
                .when(mockUserCrudOperations)
                .createEntity(user);

        doReturn(token)
                .when(mockJwtUtility)
                .generateToken(getUserList.get(0).getAlias(), getUserList.get(0).getEmail());

        service.registerUser(userRegistration);
    }

    @Test
    public void shouldReturnCorrectUsersWhenRegisteringUniqueUser() {
        String hashPassword = "abc123";
        String token = "tokentokentokentokentoken";

        UserBuilder builder = UserBuilder.builder()
                .alias("timmy")
                .password("1234567890abcdef")
                .email("valid@email.com");

        UserRegistration userRegistration = builder.buildUserRegistrationDomain();

        User user = builder.buildDomain();
        user.setHashPassword(hashPassword);

        User createdUser = builder.buildDomain();

        doReturn(false)
                .when(mockUserRepository)
                .existsByAliasIgnoreCase(userRegistration.getAlias());

        doReturn(false)
                .when(mockUserRepository)
                .existsByEmailIgnoreCase(userRegistration.getEmail());

        doReturn(hashPassword)
                .when(mockHashingUtility)
                .hashPassword(userRegistration.getPassword());

        doReturn(createdUser)
                .when(mockUserCrudOperations)
                .createEntity(user);

        doReturn(token)
                .when(mockJwtUtility)
                .generateToken(createdUser.getAlias(), createdUser.getEmail());

        UserProfile userProfile = builder.buildUserProfileDomain();

        TokenResponse expected = new TokenResponse(userProfile, token);
        TokenResponse actual = service.registerUser(userRegistration);

        assertEquals(expected, actual);
    }

}
