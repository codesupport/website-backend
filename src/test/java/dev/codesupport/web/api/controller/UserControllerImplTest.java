package dev.codesupport.web.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.domain.User;
import dev.codesupport.testutils.builders.UserBuilder;
import dev.codesupport.web.api.service.UserService;
import dev.codesupport.web.domain.UserRegistration;
import dev.codesupport.web.domain.UserStripped;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
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
    public void setup() {
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

        List<UserStripped> returnedUsers = mapper().convertValue(userList, new TypeReference<List<UserStripped>>() {
        });

        doReturn(returnedUsers)
                .when(mockService)
                .findAllUsers();

        RestResponse<UserStripped> expected = new RestResponse<>(returnedUsers);
        RestResponse<UserStripped> actual = controller.getAllUsers();

        assertEquals(expected.getResponse(), actual.getResponse());
    }

    @Test
    public void shouldReturnCorrectResultsForGetUserById() {
        long id = 1L;

        List<User> userList = userBuilders.stream()
                .map(UserBuilder::buildDomain).collect(Collectors.toList());

        List<UserStripped> returnedUsers = mapper().convertValue(userList, new TypeReference<List<UserStripped>>() {
        });

        doReturn(returnedUsers)
                .when(mockService)
                .getUserById(id);

        RestResponse<UserStripped> expected = new RestResponse<>(returnedUsers);
        RestResponse<UserStripped> actual = controller.getUserById(id);

        assertEquals(expected.getResponse(), actual.getResponse());
    }

    @Test
    public void shouldReturnCorrectResponseForRegisterUser() {
        List<String> token = Collections.singletonList("Tokentokentoken");

        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setAlias("user");

        doReturn(token)
                .when(mockService)
                .registerUser(userRegistration);

        RestResponse<String> expected = new RestResponse<>(token);
        RestResponse<String> actual = controller.registerUser(userRegistration);

        assertEquals(expected.getResponse(), actual.getResponse());
    }

}
