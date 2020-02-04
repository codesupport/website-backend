package dev.codesupport.web.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.codesupport.testutils.builders.UserBuilder;
import dev.codesupport.web.api.service.UserService;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserProfileStripped;
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

public class UserProfileControllerImplTest {

    private static ObjectMapper mapper;

    private static UserProfileControllerImpl controller;

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

        controller = new UserProfileControllerImpl(mockService);
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
    public void shouldReturnCorrectResultsForGetAllUserProfiles() {
        List<User> userList = userBuilders.stream()
                .map(UserBuilder::buildDomain).collect(Collectors.toList());

        List<UserProfileStripped> expected = mapper()
                .convertValue(userList, new TypeReference<List<UserProfileStripped>>() {
                });

        doReturn(expected)
                .when(mockService)
                .findAllUserProfiles();

        List<UserProfileStripped> actual = controller.getAllUserProfiles();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResultsForGetUserProfileByAlias() {
        String alias = "Username";

        List<User> userList = userBuilders.stream()
                .map(UserBuilder::buildDomain).collect(Collectors.toList());

        UserProfile expected = mapper()
                .convertValue(userList.get(0), UserProfile.class);

        doReturn(expected)
                .when(mockService)
                .getUserProfileByAlias(alias);

        UserProfile actual = controller.getUserProfileByAlias(alias);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResultsForGetUserProfileById() {
        long id = 1L;

        List<User> userList = userBuilders.stream()
                .map(UserBuilder::buildDomain).collect(Collectors.toList());

        List<UserProfileStripped> returnedUsers = mapper()
                .convertValue(userList, new TypeReference<List<UserProfileStripped>>() {
                });

        doReturn(returnedUsers.get(0))
                .when(mockService)
                .getUserProfileById(id);

        UserProfileStripped expected = returnedUsers.get(0);
        UserProfileStripped actual = controller.getUserProfileById(id);

        assertEquals(expected, actual);
    }

}
