package dev.codesupport.web.common.service.http;

import dev.codesupport.web.common.exception.HttpRequestException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Error handler to be used with the RestTemplate
 */
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    /**
     * Determines if the response has an issue that needs to be handled.
     * <p>Checks if the response contains anything but a 200 level status code.</p>
     *
     * @param clientHttpResponse The response to check
     * @return False if the response status code is a 2xx level, True otherwise
     * @throws IOException If the response is otherwise unreadable
     */
    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        boolean hasError = false;
        if (!clientHttpResponse.getStatusCode().is2xxSuccessful()) {
            hasError = true;
        }
        return hasError;
    }

    /**
     * Handles the error if hasError() returned True.
     * <p>Parses the message and adds it to a thrown exception.</p>
     *
     * @param clientHttpResponse The response message with the error.
     * @throws IOException If the response message can not be read.
     * @throws HttpRequestException With the response's body inside for logging.
     */
    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(clientHttpResponse.getBody(), StandardCharsets.UTF_8));
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append('\n');
            line = br.readLine();
        }

        throw new HttpRequestException("Http call failed: " + sb.toString().trim());
    }

}
