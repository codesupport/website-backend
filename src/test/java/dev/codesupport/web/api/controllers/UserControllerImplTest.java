package dev.codesupport.web.api.controllers;

import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.domain.User;
import dev.codesupport.testutils.builders.UserBuilder;
import dev.codesupport.web.api.controller.UserControllerImpl;
import dev.codesupport.web.api.service.UserService;
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

    private static UserControllerImpl controller;

    private static UserService mockService;

    private static List<UserBuilder> userBuilders;

    @BeforeClass
    public static void init() {
        userBuilders = Collections.singletonList(
                UserBuilder.builder()
                        .id(5L)
                        .username("timmy")
                        .password("1234567890abcdef")
                        .email("valid@email.com")
                        .addedBy(2L)
                        .avatarLink("timmeh.jpg")
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

    @Test
    public void shouldReturnCorrectResultsForGetAllUsers() {
        List<User> returnedUsers = userBuilders.stream()
                .map(UserBuilder::buildDomain).collect(Collectors.toList());

        doReturn(returnedUsers)
                .when(mockService)
                .findAllUsers();

        RestResponse<User> expected = new RestResponse<>(returnedUsers);
        RestResponse<User> actual = controller.getAllUsers();

        assertEquals(expected.getResponse(), actual.getResponse());
    }

    @Test
    public void shouldReturnCorrectResultsForGetUserById() {
        long id = 1L;

        List<User> returnedUsers = userBuilders.stream()
                .map(UserBuilder::buildDomain).collect(Collectors.toList());

        doReturn(returnedUsers)
                .when(mockService)
                .getUserById(id);

        RestResponse<User> expected = new RestResponse<>(returnedUsers);
        RestResponse<User> actual = controller.getUserById(id);

        assertEquals(expected.getResponse(), actual.getResponse());
    }

    @Test
    public void shouldReturnCorrectListOfUsersForCreateUsers() {
        List<User> returnedUsers = userBuilders.stream()
                .map(UserBuilder::buildDomain).collect(Collectors.toList());

        List<User> givenUsers = userBuilders.stream()
                .map(UserBuilder::buildDomain).collect(Collectors.toList());

        doReturn(returnedUsers)
                .when(mockService)
                .createUsers(givenUsers);

        RestResponse<User> expected = new RestResponse<>(returnedUsers);
        RestResponse<User> actual = controller.createUsers(givenUsers);

        assertEquals(expected.getResponse(), actual.getResponse());
    }

    @Test
    public void shouldReturnCorrectListOfUsersForUpdatedUsers() {
        List<User> returnedUsers = userBuilders.stream()
                .map(UserBuilder::buildDomain).collect(Collectors.toList());

        List<User> givenUsers = userBuilders.stream()
                .map(UserBuilder::buildDomain).collect(Collectors.toList());

        doReturn(returnedUsers)
                .when(mockService)
                .updateUsers(givenUsers);

        RestResponse<User> expected = new RestResponse<>(returnedUsers);
        RestResponse<User> actual = controller.updateUsers(givenUsers);

        assertEquals(expected.getResponse(), actual.getResponse());
    }

    @Test
    public void shouldReturnCorrectListOfUsersForDeleted() {
        List<User> givenUsers = userBuilders.stream()
                .map(UserBuilder::buildDomain).collect(Collectors.toList());

        doReturn(Collections.emptyList())
                .when(mockService)
                .updateUsers(givenUsers);

        RestResponse<User> expected = new RestResponse<>(Collections.emptyList());
        RestResponse<User> actual = controller.deleteUsers(givenUsers);

        assertEquals(expected.getResponse(), actual.getResponse());
    }

}
