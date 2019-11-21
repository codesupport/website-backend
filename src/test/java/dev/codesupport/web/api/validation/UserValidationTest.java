package dev.codesupport.web.api.validation;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.entity.UserEntity_;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.domain.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class UserValidationTest {

    private static UserRepository mockUserRepository;

    private static UserValidation validation;

    @BeforeClass
    public static void init() {
        mockUserRepository = mock(UserRepository.class);

        validation = new UserValidation(mockUserRepository);
    }

    @Before
    public void setup() {
        Mockito.reset(
                mockUserRepository
        );
    }

    @Test
    public void shouldReturnNoValidationIssuesIfUserDoesntExist() {
        String username = "phil";
        User user = new User();
        user.setUsername(username);

        doReturn(false)
                .when(mockUserRepository)
                .existsByUsername(username);

        List<ValidationIssue> actual = validation.validate(user);

        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void shouldReturnValidationIssuesIfUserDoesAlreadyExist() {
        String username = "phil";
        User userSpy = spy(User.class);
        userSpy.setUsername(username);

        ValidationIssue mockValidationIssue = mock(ValidationIssue.class);

        doReturn(mockValidationIssue)
                .when(userSpy)
                .createDuplicateParameter(null, UserEntity_.USERNAME);

        doReturn(true)
                .when(mockUserRepository)
                .existsByUsername(username);

        List<ValidationIssue> actual = validation.validate(userSpy);

        assertEquals(Collections.singletonList(mockValidationIssue), actual);
    }

    @Test
    public void shouldReturnExpectedEntityType() {
        assertEquals(validation.getEntityType(), UserEntity.class);
    }

}
