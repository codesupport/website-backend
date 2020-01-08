package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.service.service.RestResponse;
import org.junit.Test;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

public class AuthenticationControllerTest {

    @Test
    public void shouldCreateCorrectRestResponseObject() {
        String referenceId = "1234";
        List<Serializable> token = Collections.singletonList("tokentokentoken");

        AuthenticationController spyController = spy(AuthenticationController.class);

        RestResponse<Serializable> expected = new RestResponse<>();
        expected.setReferenceId(referenceId);
        expected.setResponse(token);

        RestResponse<Serializable> actual = spyController.getRestResponse(token);
        actual.setReferenceId(referenceId);

        assertEquals(expected, actual);
    }

}
