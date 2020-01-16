package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.security.models.AuthenticationRequest;
import dev.codesupport.web.common.security.AuthorizationService;
import dev.codesupport.web.common.service.service.RestResponse;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

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

        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);

        doThrow(InvalidUserException.class)
                .when(mockAuthorizationService)
                .createTokenForEmailAndPassword(
                        request.getEmail(),
                        request.getPassword()
                );

        AuthenticationControllerImpl controller = new AuthenticationControllerImpl(mockAuthorizationService);

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

        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);

        doReturn(token)
                .when(mockAuthorizationService)
                .createTokenForEmailAndPassword(
                        request.getEmail(),
                        request.getPassword()
                );

        AuthenticationControllerImpl spyController = spy(new AuthenticationControllerImpl(mockAuthorizationService));

        //unchecked - This is fine for creating mocks.
        //noinspection unchecked
        RestResponse<Serializable> mockRestResponse = mock(RestResponse.class);

        doReturn(mockRestResponse)
                .when(spyController)
                .getRestResponse(Collections.singletonList(token));

        ResponseEntity<RestResponse<Serializable>> response = spyController.authenticate(request);

        assertEquals(mockRestResponse, response.getBody());
    }

}
