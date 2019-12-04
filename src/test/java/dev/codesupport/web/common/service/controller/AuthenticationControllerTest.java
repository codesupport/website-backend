package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.security.AuthenticationRequest;
import dev.codesupport.web.common.security.JwtUtility;
import dev.codesupport.web.common.service.service.AuthenticationUserDetailsService;
import dev.codesupport.web.common.service.service.RestResponse;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

//DefaultAnnotationParam - If not annotated, Sonar complains there is no assertion made, would rather be explicit
@SuppressWarnings("DefaultAnnotationParam")
public class AuthenticationControllerTest {

    @Test
    public void shouldReturnTokenIfAuthenticatedUser() {
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        AuthenticationUserDetailsService mockAuthenticationUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JwtUtility mockJwtUtility = mock(JwtUtility.class);

        AuthenticationRequest mockAuthenticationRequest = mock(AuthenticationRequest.class);
        UserDetails mockUserDetails = mock(UserDetails.class);

        String referenceId = "12345";
        String username = "testuser";
        String password = "password123";

        List<Serializable> tokenList = Collections.singletonList("1234");

        RestResponse<Serializable> restResponse = new RestResponse<>(tokenList);
        restResponse.setReferenceId(referenceId);

        //ResultOfMethodCallIgnored - Not using results, we're creating mocks
        //noinspection ResultOfMethodCallIgnored
        doReturn(username)
                .when(mockAuthenticationRequest)
                .getUsername();

        //ResultOfMethodCallIgnored - Not using results, we're creating mocks
        //noinspection ResultOfMethodCallIgnored
        doReturn(password)
                .when(mockAuthenticationRequest)
                .getPassword();

        doReturn(username)
                .when(mockUserDetails)
                .getUsername();

        doReturn(mockUserDetails)
                .when(mockAuthenticationUserDetailsService)
                .loadUserByUsername(username);

        doReturn(tokenList.get(0))
                .when(mockJwtUtility)
                .generateToken(username);

        AuthenticationController controllerSpy = spy(
                new AuthenticationController(
                        mockAuthenticationManager,
                        mockAuthenticationUserDetailsService,
                        mockJwtUtility
                )
        );

        doReturn(restResponse)
                .when(controllerSpy)
                .getRestResponse(tokenList);

        doNothing()
                .when(controllerSpy)
                .authenticate(username, password);

        RestResponse<Serializable> expectedResponse = new RestResponse<>(tokenList);
        expectedResponse.setReferenceId(referenceId);

        ResponseEntity<RestResponse<Serializable>> expected = ResponseEntity.ok(expectedResponse);
        ResponseEntity<RestResponse<Serializable>> actual = controllerSpy.createAuthenticationToken(mockAuthenticationRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectRestResponseInstance() {
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        AuthenticationUserDetailsService mockAuthenticationUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JwtUtility mockJwtUtility = mock(JwtUtility.class);

        AuthenticationController controller = new AuthenticationController(
                mockAuthenticationManager,
                mockAuthenticationUserDetailsService,
                mockJwtUtility
        );

        List<Serializable> expected = Collections.singletonList("string");

        List<Serializable> actual = controller.getRestResponse(expected).getResponse();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnNewRestResponseInstance() {
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        AuthenticationUserDetailsService mockAuthenticationUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JwtUtility mockJwtUtility = mock(JwtUtility.class);

        AuthenticationController controller = new AuthenticationController(
                mockAuthenticationManager,
                mockAuthenticationUserDetailsService,
                mockJwtUtility
        );

        List<Serializable> strings = Collections.singletonList("string");

        RestResponse<Serializable> firstInstance = controller.getRestResponse(strings);
        RestResponse<Serializable> secondInstance = controller.getRestResponse(strings);

        assertNotSame(firstInstance, secondInstance);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionOnDisabledExceptions() {
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        AuthenticationUserDetailsService mockAuthenticationUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JwtUtility mockJwtUtility = mock(JwtUtility.class);

        AuthenticationController controller = new AuthenticationController(
                mockAuthenticationManager,
                mockAuthenticationUserDetailsService,
                mockJwtUtility
        );

        String username = "testuser";
        String password = "password123";

        doThrow(DisabledException.class)
                .when(mockAuthenticationManager)
                .authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );

        controller.authenticate(username, password);
    }

    @Test(expected = BadCredentialsException.class)
    public void shouldBubbleBadCredentialExceptions() {
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        AuthenticationUserDetailsService mockAuthenticationUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JwtUtility mockJwtUtility = mock(JwtUtility.class);

        AuthenticationController controller = new AuthenticationController(
                mockAuthenticationManager,
                mockAuthenticationUserDetailsService,
                mockJwtUtility
        );

        String username = "testuser";
        String password = "password123";

        doThrow(BadCredentialsException.class)
                .when(mockAuthenticationManager)
                .authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );

        controller.authenticate(username, password);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfAuthenticationIsNull() {
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        AuthenticationUserDetailsService mockAuthenticationUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JwtUtility mockJwtUtility = mock(JwtUtility.class);

        AuthenticationController controller = new AuthenticationController(
                mockAuthenticationManager,
                mockAuthenticationUserDetailsService,
                mockJwtUtility
        );

        String username = "testuser";
        String password = "password123";

        doReturn(null)
                .when(mockAuthenticationManager)
                .authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );

        controller.authenticate(username, password);
    }

    @Test(expected = Test.None.class)
    public void shouldThrowNoExceptionsIfValidAuthentication() {
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        AuthenticationUserDetailsService mockAuthenticationUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        Authentication mockAuthentication = mock(Authentication.class);

        AuthenticationController controller = new AuthenticationController(
                mockAuthenticationManager,
                mockAuthenticationUserDetailsService,
                mockJwtUtility
        );

        String username = "testuser";
        String password = "password123";

        doReturn(mockAuthentication)
                .when(mockAuthenticationManager)
                .authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );

        controller.authenticate(username, password);
    }

}
