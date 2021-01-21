package dev.codesupport.web.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.codesupport.testutils.builders.UserBuilder;
import dev.codesupport.web.api.service.UserService;
import dev.codesupport.web.domain.Permission;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserRegistration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class UserControllerImplTest {

    private static ObjectMapper mapper;

    private static UserControllerImpl controller;

    private static UserService mockService;

    private static List<UserBuilder> userBuilders;

    @BeforeClass
    public static void init() {
        mapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        userBuilders = Collections.singletonList(
                UserBuilder.builder()
                        .id(5L)
                        .alias("timmy")
                        .hashPassword("1234567890abcdef")
                        .email("valid@email.com")
                        .avatarLink("timmeh.jpg")
                        .joinDate(1L)
        );

        mockService = mock(UserService.class);

        controller = new UserControllerImpl(mockService);
    }

    @Before
    public void setUp() {
        Mockito.reset(
                mockService
        );
    }

    private ObjectMapper mapper() {
        return mapper;
    }

    @Test
    public void shouldReturnCorrectResultsForGetAllUsers() {
        List<User> userList = userBuilders.stream()
                .map(UserBuilder::buildDomain).collect(Collectors.toList());

        List<User> expected = mapper().convertValue(userList, new TypeReference<List<User>>() {
        });

        doReturn(expected)
                .when(mockService)
                .findAllUsers();

        List<User> actual = controller.getAllUsers();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResultsForGetUserById() {
        long id = 1L;

        List<User> userList = userBuilders.stream()
                .map(UserBuilder::buildDomain).collect(Collectors.toList());

        List<User> returnedUsers = mapper().convertValue(userList, new TypeReference<List<User>>() {
        });

        doReturn(returnedUsers.get(0))
                .when(mockService)
                .getUserById(id);

        User expected = returnedUsers.get(0);
        User actual = controller.getUserById(id);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResultsForGetUserPermissions() {
        long id = 10L;

        Set<Permission> expected = new HashSet<>(
                Arrays.asList(
                        mock(Permission.class),
                        mock(Permission.class)
                )
        );

        doReturn(expected)
                .when(mockService)
                .getUserPermissionsById(id);

        Set<Permission> actual = controller.getUserPermissions(id);

        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResponseForRegisterUser() {
        UserProfile expected = mock(UserProfile.class);

        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setAlias("user");

        doReturn(expected)
                .when(mockService)
                .registerUser(userRegistration);

        UserProfile actual = controller.registerUser(userRegistration);

        assertEquals(expected, actual);
    }

}
