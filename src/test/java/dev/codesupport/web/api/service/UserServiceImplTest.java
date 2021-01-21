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
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.domain.NewUser;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserRegistration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class UserServiceImplTest {

    private static ObjectMapper mapper;

    private static UserServiceImpl service;

    private static HashingUtility mockHashingUtility;

    private static CrudOperations<UserEntity, NewUser, Long> mockUserCrudOperations;
    private static CrudOperations<UserEntity, UserProfile, Long> mockUserProfileCrudOperations;

    private static UserRepository mockUserRepository;

    private static List<NewUser> expectedUserList;

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

        ApplicationContext mockContext = mock(ApplicationContext.class);

        ReflectionTestUtils.setField(mockUserCrudOperations, "context", mockContext);
        ReflectionTestUtils.setField(mockUserProfileCrudOperations, "context", mockContext);

        mockHashingUtility = mock(HashingUtility.class);

        service = new UserServiceImpl(mockUserRepository, mockHashingUtility);
        ReflectionTestUtils.setField(service, "userCrudOperations", mockUserCrudOperations);
        ReflectionTestUtils.setField(service, "userProfileCrudOperations", mockUserProfileCrudOperations);

        UserBuilder userBuilder = UserBuilder.builder()
            .id(5L)
            .alias("timmy")
            .hashPassword("1234567890abcdef")
            .email("valid@email.com")
            .avatarLink("timmeh.jpg")
            .joinDate(1L);

        expectedUserList = Collections.singletonList(userBuilder.buildNewUserDomain());
    }

    @Before
    public void setUp() {
        Mockito.reset(
                mockUserCrudOperations,
                mockUserProfileCrudOperations,
                mockUserRepository,
                mockHashingUtility
        );
    }

    private ObjectMapper mapper() {
        return mapper;
    }

    @Test
    public void shouldReturnCorrectUsersWithFindAllUserProfiles() {
        List<UserProfile> expected = mapper()
                .convertValue(expectedUserList, new TypeReference<List<UserProfile>>() {
                });

        doReturn(expected)
                .when(mockUserProfileCrudOperations)
                .getAll();

        List<UserProfile> actual = service.findAllUserProfiles();

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
                .convertValue(expectedUserList.get(0), UserProfile.class);

        UserEntity userEntity = mapper()
                .convertValue(expectedUserList.get(0), UserEntity.class);

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

        List<UserProfile> returnedUsers = mapper()
                .convertValue(expectedUserList, new TypeReference<List<UserProfile>>() {
                });

        doReturn(returnedUsers.get(0))
                .when(mockUserProfileCrudOperations)
                .getById(id);

        UserProfile expected = returnedUsers.get(0);
        UserProfile actual = service.getUserProfileById(id);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectUsersWithFindAllUsers() {
        List<User> expected = mapper()
                .convertValue(expectedUserList, new TypeReference<List<User>>() {
                });

        doReturn(expectedUserList)
                .when(mockUserCrudOperations)
                .getAll();

        List<User> actual = service.findAllUsers();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectUsersWithGetUserById() {
        Long id = 1L;

        List<User> returnedUsers = mapper()
                .convertValue(expectedUserList, new TypeReference<List<User>>() {
                });

        doReturn(expectedUserList.get(0))
                .when(mockUserCrudOperations)
                .getById(id);

        User expected = returnedUsers.get(0);
        User actual = service.getUserById(id);

        assertEquals(expected, actual);
    }

    @Test(expected = ServiceLayerException.class)
    public void shouldThrowServiceLayerExceptionIfRegisteringWithNonUniqueAlias() {
        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setAlias("timmy");
        userRegistration.setPassword("1234567890abcdef");
        userRegistration.setEmail("valid@email.com");

        NewUser user = mapper().convertValue(userRegistration, NewUser.class);

        doReturn(true)
                .when(mockUserRepository)
                .existsByAliasIgnoreCase(userRegistration.getAlias());

        doReturn(false)
                .when(mockUserRepository)
                .existsByEmailIgnoreCase(userRegistration.getEmail());

        doReturn(expectedUserList.get(0))
                .when(mockUserCrudOperations)
                .createEntity(user);

        service.registerUser(userRegistration);
    }

    @Test(expected = ServiceLayerException.class)
    public void shouldThrowServiceLayerExceptionIfRegisterWithNonUniqueEmail() {
        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setAlias("timmy");
        userRegistration.setPassword("1234567890abcdef");
        userRegistration.setEmail("valid@email.com");

        NewUser user = mapper().convertValue(userRegistration, NewUser.class);

        doReturn(false)
                .when(mockUserRepository)
                .existsByAliasIgnoreCase(userRegistration.getAlias());

        doReturn(true)
                .when(mockUserRepository)
                .existsByEmailIgnoreCase(userRegistration.getEmail());

        doReturn(expectedUserList.get(0))
                .when(mockUserCrudOperations)
                .createEntity(user);

        service.registerUser(userRegistration);
    }

    @Test
    public void shouldReturnCorrectUsersWhenRegisteringUniqueUser() {
        String hashPassword = "abc123";

        UserBuilder builder = UserBuilder.builder()
                .alias("timmy")
                .password("1234567890abcdef")
                .email("valid@email.com");

        UserRegistration userRegistration = builder.buildUserRegistrationDomain();

        NewUser user = mapper()
                .convertValue(userRegistration, NewUser.class);

        user.setHashPassword(hashPassword);

        NewUser createdUser = mapper()
                .convertValue(userRegistration, NewUser.class);

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

        UserProfile expected = builder.buildUserProfileDomain();
        UserProfile actual = service.registerUser(userRegistration);

        assertEquals(expected, actual);
    }

}
