package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.common.security.AuthenticationService;
import dev.codesupport.web.common.security.models.AuthenticationRequest;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//S2068 - This is not a real password
@SuppressWarnings("squid:S2068")
public class AuthenticationControllerImplTest {

    @Test(expected = InvalidUserException.class)
    public void shouldBubbleExceptionThrownByInvalidCredentials() {
        String email = "user@domain.com";
        String password = "clearpassword";

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(email);
        request.setPassword(password);

        AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);

        doThrow(InvalidUserException.class)
                .when(mockAuthenticationService)
                .authenticate(
                        request.getEmail(),
                        request.getPassword()
                );

        AuthenticationControllerImpl controller = new AuthenticationControllerImpl(mockAuthenticationService);

        controller.authenticate(request);
    }

    @Test
    public void shouldReturnTokenIfAuthenticatedCredentials() {
        String email = "user@domain.com";
        String password = "clearpassword";
        String token = "mytokenstring";

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(email);
        request.setPassword(password);

        AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);

        doReturn(token)
                .when(mockAuthenticationService)
                .authenticate(
                        request.getEmail(),
                        request.getPassword()
                );

        AuthenticationControllerImpl spyController = spy(new AuthenticationControllerImpl(mockAuthenticationService));

        ResponseEntity<Void> expected = ResponseEntity.ok().header("Set-Cookie", token).body(null);
        ResponseEntity<Void> actual = spyController.authenticate(request);

        assertEquals(expected, actual);
    }

    @Test(expected = ServiceLayerException.class)
    public void shouldBubbleUpExceptionThrownByAuthorizationService() {
        String code = "code";

        AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);

        doThrow(ServiceLayerException.class)
                .when(mockAuthenticationService)
                .linkDiscord(code);

        AuthenticationControllerImpl controller = new AuthenticationControllerImpl(mockAuthenticationService);

        controller.linkDiscord(code);
    }

    @Test
    public void shouldInvokeLinkDiscord() {
        String code = "code";

        AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);

        AuthenticationControllerImpl controller = new AuthenticationControllerImpl(mockAuthenticationService);

        controller.linkDiscord(code);

        verify(mockAuthenticationService, times(1))
                .linkDiscord(code);
    }

    @Test
    public void shouldReturnOkForLinkDiscord() {
        String code = "code";

        AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);

        AuthenticationControllerImpl controller = new AuthenticationControllerImpl(mockAuthenticationService);

        ResponseEntity<Void> expected = ResponseEntity.ok().body(null);
        ResponseEntity<Void> actual = controller.linkDiscord(code);

        assertEquals(expected, actual);
    }

}
