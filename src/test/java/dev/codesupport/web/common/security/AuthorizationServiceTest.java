package dev.codesupport.web.common.security;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.exception.InvalidUserException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class AuthorizationServiceTest {

    private static AuthorizationService spyService;

    private static UserRepository mockUserRepository;

    private static HashingUtility mockHashingUtility;

    private static JwtUtility mockJwtUtility;

    @BeforeClass
    public static void init() {
        mockUserRepository = mock(UserRepository.class);
        mockHashingUtility = mock(HashingUtility.class);
        mockJwtUtility = mock(JwtUtility.class);

        spyService = spy(new AuthorizationService(mockUserRepository, mockHashingUtility, mockJwtUtility));
    }

    @Before
    public void setup() {
        Mockito.reset(
                mockUserRepository,
                mockHashingUtility,
                mockJwtUtility,
                spyService
        );
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfUserDisabled() {
        String alias = "me";
        String email = "email@email.em";
        String password = "mypassword";
        String token = "tokentokentoken";

        UserDetails userDetails = new UserDetails(alias, password, email, Collections.emptySet(), true);

        doReturn(userDetails)
                .when(spyService)
                .getUserDetailsByEmail(email);

        doReturn(true)
                .when(mockHashingUtility)
                .verifyPassword(password, password);

        doReturn(token)
                .when(mockJwtUtility)
                .generateToken(
                        alias,
                        email
                );

        spyService.createTokenForEmailAndPassword(email, password);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfInvalidPassword() {
        String alias = "me";
        String email = "email@email.em";
        String password = "mypassword";
        String token = "tokentokentoken";

        UserDetails userDetails = new UserDetails(alias, password, email, Collections.emptySet(), false);

        doReturn(userDetails)
                .when(spyService)
                .getUserDetailsByEmail(email);

        doReturn(false)
                .when(mockHashingUtility)
                .verifyPassword(password, password);

        doReturn(token)
                .when(mockJwtUtility)
                .generateToken(
                        alias,
                        email
                );

        spyService.createTokenForEmailAndPassword(email, password);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfGetUserDetailsThrowsInvalidUserException() {
        String alias = "me";
        String email = "email@email.em";
        String password = "mypassword";
        String token = "tokentokentoken";

        doThrow(InvalidUserException.class)
                .when(spyService)
                .getUserDetailsByEmail(email);

        doReturn(true)
                .when(mockHashingUtility)
                .verifyPassword(password, password);

        doReturn(token)
                .when(mockJwtUtility)
                .generateToken(
                        alias,
                        email
                );

        spyService.createTokenForEmailAndPassword(email, password);
    }

    @Test
    public void shouldCreateTokenForValidEmailAndPassword() {
        String alias = "me";
        String email = "email@email.em";
        String password = "mypassword";
        String token = "tokentokentoken";

        UserDetails userDetails = new UserDetails(alias, password, email, Collections.emptySet(), false);

        doReturn(userDetails)
                .when(spyService)
                .getUserDetailsByEmail(email);

        doReturn(true)
                .when(mockHashingUtility)
                .verifyPassword(password, password);

        doReturn(token)
                .when(mockJwtUtility)
                .generateToken(
                        alias,
                        email
                );

        String actual = spyService.createTokenForEmailAndPassword(email, password);

        assertEquals(token, actual);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfUserNotFound() {
        String email = "email@email.em";

        doReturn(null)
                .when(mockUserRepository)
                .findByEmail(email);

        spyService.getUserDetailsByEmail(email);
    }

    @Test
    public void shouldReturnCorrectUserDetailsIfUserFound() {
        String alias = "me";
        String email = "email@email.em";
        String password = "mypassword";

        UserEntity userEntity = new UserEntity();
        userEntity.setAlias(alias);
        userEntity.setEmail(email);
        userEntity.setHashPassword(password);
        userEntity.setPermission(Collections.emptySet());
        userEntity.setDisabled(false);

        doReturn(userEntity)
                .when(mockUserRepository)
                .findByEmail(email);

        UserDetails expected = new UserDetails(alias, password, email, Collections.emptySet(), false);

        UserDetails actual = spyService.getUserDetailsByEmail(email);

        assertEquals(expected, actual);
    }

}
