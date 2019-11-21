package dev.codesupport.web.api.controllers;

import dev.codesupport.web.api.controller.UserController;
import dev.codesupport.web.domain.User;
import dev.codesupport.testutils.builders.UserBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//DefaultAnnotationParam - If not annotated, Sonar complains there is no assertion made, would rather be explicit
@SuppressWarnings("DefaultAnnotationParam")
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    //unused - IDE can't see that spring is autowiring this.
    @SuppressWarnings("unused")
    @Qualifier("mockUserController")
    @Autowired
    private UserController controller;

    private static List<User> mockUserListValid;

    @BeforeClass
    public static void init() {
        List<UserBuilder> userBuilderList = Collections.singletonList(
                UserBuilder.builder()
                        .id(5L)
                        .username("timmy")
                        .password("1234567890abcdef")
                        .email("valid@email.com")
                        .addedBy(2L)
                        .avatarLink("timmeh.jpg")
        );

        mockUserListValid = userBuilderList.stream().map(UserBuilder::buildDomain).collect(Collectors.toList());
    }

    @Test(expected = Test.None.class)
    public void shouldProduceNoValidationIssuesForGetAllUsers() {
        //Would rather suppress here then on the controller interface
        //noinspection unused
        controller.getAllUsers();
    }

    @Test(expected = Test.None.class)
    public void shouldProduceNoValidationIssuesForGetUserById() {
        long id = 1;

        //Would rather suppress here then on the controller interface
        //noinspection unused
        controller.getUserById(id);
    }

    @Test(expected = Test.None.class)
    public void shouldProduceNoValidationIssuesForCreateUsers() {
        //Would rather suppress here then on the controller interface
        //noinspection unused
        controller.createUsers(mockUserListValid);
    }

    @Test(expected = Test.None.class)
    public void shouldProduceNoValidationIssuesForUpdateUsers() {
        //Would rather suppress here then on the controller interface
        //noinspection unused
        controller.updateUsers(mockUserListValid);
    }

    @Test(expected = Test.None.class)
    public void shouldProduceNoValidationIssuesForDeleteUsers() {
        //Would rather suppress here then on the controller interface
        //noinspection unused
        controller.deleteUsers(mockUserListValid);
    }

}
