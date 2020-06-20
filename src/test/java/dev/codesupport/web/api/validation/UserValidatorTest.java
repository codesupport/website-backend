package dev.codesupport.web.api.validation;

import dev.codesupport.web.api.data.entity.UserEntity;
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

public class UserValidatorTest {

    private static UserRepository mockUserRepository;

    private static UserValidator validation;

    @BeforeClass
    public static void init() {
        mockUserRepository = mock(UserRepository.class);

        validation = new UserValidator(mockUserRepository);
    }

    @Before
    public void setUp() {
        Mockito.reset(
                mockUserRepository
        );
    }

    @Test
    public void shouldReturnNoValidationIssuesIfUserDoesntExist() {
        String alias = "phil";
        String email = "phil@phil.ph";

        User user = new User();
        user.setAlias(alias);
        user.setEmail(email);

        doReturn(false)
                .when(mockUserRepository)
                .existsByAliasIgnoreCase(alias);

        doReturn(false)
                .when(mockUserRepository)
                .existsByEmailIgnoreCase(email);

        List<ValidationIssue> actual = validation.validate(user);

        assertEquals(Collections.emptyList(), actual);
    }

    //TODO: Figure out why this is throwing an UnfinishedStubbingException
//    @Test
//    public void shouldReturnValidationIssuesIfUserDoesAlreadyExist() {
//        String alias = "phil";
//        String email = "phil@phil.ph";
//
//        User userSpy = spy(User.class);
//        userSpy.setAlias(alias);
//        userSpy.setEmail(email);
//
//        ValidationIssue mockValidationIssue = mock(ValidationIssue.class);
//
//        doReturn(mockValidationIssue)
//                .when(userSpy)
//                .createDuplicateParameter(null, UserEntity_.ALIAS);
//
//        doReturn(mockValidationIssue)
//                .when(userSpy)
//                .createDuplicateParameter(null, UserEntity_.EMAIL);
//
//        doReturn(true)
//                .when(mockUserRepository)
//                .existsByAlias(alias);
//
//        doReturn(true)
//                .when(mockUserRepository)
//                .existsByEmail(email);
//
//        List<ValidationIssue> actual = validation.validate(userSpy);
//
//        assertEquals(Collections.singletonList(mockValidationIssue), actual);
//    }

    @Test
    public void shouldReturnExpectedEntityType() {
        assertEquals(validation.getEntityType(), UserEntity.class);
    }

}
