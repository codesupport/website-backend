package dev.codesupport.web.api.validation;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.domain.NewUser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NewUserValidatorTest {

    private static UserRepository mockUserRepository;

    private static NewUserValidator validation;

    @BeforeClass
    public static void init() {
        mockUserRepository = mock(UserRepository.class);

        validation = new NewUserValidator(mockUserRepository);
    }

    @Before
    public void setUp() {
        Mockito.reset(
                mockUserRepository
        );
    }

    @Test
    public void shouldReturnNoValidationIssuesIfUserDoesNotExist() {
        String alias = "phil";
        String email = "phil@phil.ph";

        NewUser user = new NewUser();
        user.setAlias(alias);
        user.setEmail(email);

        doReturn(false)
                .when(mockUserRepository)
                .existsByAliasIgnoreCase(alias);

        doReturn(false)
                .when(mockUserRepository)
                .existsByEmailIgnoreCase(email);

        Set<ValidationIssue> actual = validation.validate(user);

        assertEquals(Collections.emptySet(), actual);
    }

    @Test
    public void shouldReturnValidationIssuesIfUserDoesAlreadyExist() {
        String alias = "phil";
        String email = "phil@phil.ph";

        NewUser userSpy = spy(NewUser.class);
        userSpy.setAlias(alias);
        userSpy.setEmail(email);

        doReturn(true)
                .when(mockUserRepository)
                .existsByAliasIgnoreCase(alias);

        doReturn(true)
                .when(mockUserRepository)
                .existsByEmailIgnoreCase(email);

        Set<ValidationIssue> expected = new HashSet<>(Arrays.asList(
                new ValidationIssue(null, ValidationIssue.ValidationType.DUPLICATE, NewUser.Fields.email, "Must be unique in database"),
                new ValidationIssue(null, ValidationIssue.ValidationType.DUPLICATE, NewUser.Fields.alias, "Must be unique in database")
        ));
        Set<ValidationIssue> actual = validation.validate(userSpy);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnExpectedEntityType() {
        assertEquals(validation.getEntityType(), UserEntity.class);
    }

}
