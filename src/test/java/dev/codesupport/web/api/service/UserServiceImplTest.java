package dev.codesupport.web.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.codesupport.testutils.builders.CountryBuilder;
import dev.codesupport.testutils.builders.UserBuilder;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.CountryRepository;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.common.security.hashing.HashingUtility;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.domain.Country;
import dev.codesupport.web.domain.NewUser;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserRegistration;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class UserServiceImplTest {

    private static ObjectMapper mapper;

    private static UserServiceImpl serviceSpy;

    private static HashingUtility mockHashingUtility;

    private static CrudOperations<UserEntity, NewUser, Long> mockUserCrudOperations;
    private static CrudOperations<UserEntity, UserProfile, Long> mockUserProfileCrudOperations;

    private static UserRepository mockUserRepository;
    private static CountryRepository mockCountryRepository;

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
        mockCountryRepository = mock(CountryRepository.class);

        mockHashingUtility = mock(HashingUtility.class);

        serviceSpy = spy(new UserServiceImpl(mockUserRepository, mockCountryRepository, mockHashingUtility));
        ReflectionTestUtils.setField(serviceSpy, "userCrudOperations", mockUserCrudOperations);
        ReflectionTestUtils.setField(serviceSpy, "userProfileCrudOperations", mockUserProfileCrudOperations);

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
                mockCountryRepository,
                mockHashingUtility,
                serviceSpy
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

        List<UserProfile> actual = serviceSpy.findAllUserProfiles();

        assertEquals(expected, actual);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionWithGetUserProfileByAliasIfAliasDoesNotExist() {
        String alias = "username";

        Optional<UserEntity> optional = Optional.empty();

        doReturn(optional)
                .when(mockUserRepository)
                .findByAliasIgnoreCase(alias);

        serviceSpy.getUserProfileByAlias(alias);
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

        UserProfile actual = serviceSpy.getUserProfileByAlias(alias);

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
        UserProfile actual = serviceSpy.getUserProfileById(id);

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

        List<User> actual = serviceSpy.findAllUsers();

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
        User actual = serviceSpy.getUserById(id);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCurrentUserWithGetUserById() {
        User expected = UserBuilder.builder()
                .id(5L)
                .alias("dude")
                .email("dude@email.com")
                .buildDomain();

        doReturn(expected)
                .when(serviceSpy)
                .getUserById(expected.getId());

        User actual = serviceSpy.getCurrentUser(UserBuilder.builder().id(expected.getId()).buildDomain());

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

        serviceSpy.registerUser(userRegistration);
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

        serviceSpy.registerUser(userRegistration);
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
        UserProfile actual = serviceSpy.registerUser(userRegistration);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnAllCountries() {
        Set<CountryBuilder> builderSet = Sets.newLinkedHashSet(
                CountryBuilder.builder()
                        .id(1L)
                        .code("US")
                        .label("United States of America"),
                CountryBuilder.builder()
                        .id(2L)
                        .code("GB")
                        .label("United Kingdom")
        );

        doReturn(builderSet.stream().map(CountryBuilder::buildEntity).collect(Collectors.toList()))
                .when(mockCountryRepository)
                .findAll();

        Set<Country> expected = builderSet.stream().map(CountryBuilder::buildDomain).collect(Collectors.toSet());
        Set<Country> actual = serviceSpy.findAllCountries();

        assertEquals(expected, actual);
    }

}
