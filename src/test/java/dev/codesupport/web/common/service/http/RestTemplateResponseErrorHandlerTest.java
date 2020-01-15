package dev.codesupport.web.common.service.http;

import com.google.common.io.CharSource;
import dev.codesupport.web.common.exception.HttpRequestException;
import org.hibernate.engine.jdbc.ReaderInputStream;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class RestTemplateResponseErrorHandlerTest {

    @Test
    public void shouldReturnFalseIfStatus2xx() throws IOException {
        RestTemplateResponseErrorHandler errorHandler = new RestTemplateResponseErrorHandler();

        ClientHttpResponse mockResponse = mock(ClientHttpResponse.class);

        HttpStatus httpStatus = HttpStatus.CREATED;

        doReturn(httpStatus)
                .when(mockResponse)
                .getStatusCode();

        assertFalse(errorHandler.hasError(mockResponse));
    }

    @Test
    public void shouldReturnTrueIfNotStatus2xx() throws IOException {
        RestTemplateResponseErrorHandler errorHandler = new RestTemplateResponseErrorHandler();

        ClientHttpResponse mockResponse = mock(ClientHttpResponse.class);

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        doReturn(httpStatus)
                .when(mockResponse)
                .getStatusCode();

        assertTrue(errorHandler.hasError(mockResponse));
    }

    @Test(expected = HttpRequestException.class)
    public void shouldThrowExceptionWhenHandlingError() throws IOException {
        String myString = "some text";

        RestTemplateResponseErrorHandler errorHandler = new RestTemplateResponseErrorHandler();

        ClientHttpResponse mockResponse = mock(ClientHttpResponse.class);

        InputStream inputStream = new ReaderInputStream(CharSource.wrap(myString).openStream());

        doReturn(inputStream)
                .when(mockResponse)
                .getBody();

        errorHandler.handleError(mockResponse);
    }

    @Test
    public void shouldThrowExceptionWithCorrectMessageWhenHandlingError() throws IOException {
        String myString = "some text";

        RestTemplateResponseErrorHandler errorHandler = new RestTemplateResponseErrorHandler();

        ClientHttpResponse mockResponse = mock(ClientHttpResponse.class);

        InputStream inputStream = new ReaderInputStream(CharSource.wrap(myString).openStream());

        doReturn(inputStream)
                .when(mockResponse)
                .getBody();

        String expected = "Http call failed: " + myString;

        String actual;

        try {
            errorHandler.handleError(mockResponse);

            actual = "failed";
        } catch (HttpRequestException e) {
            actual = e.getMessage();
        }

        assertEquals(expected, actual);
    }

}
