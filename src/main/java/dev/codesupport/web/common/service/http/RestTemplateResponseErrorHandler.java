package dev.codesupport.web.common.service.http;

import dev.codesupport.web.common.exception.HttpRequestException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        boolean hasError = false;
        if (!clientHttpResponse.getStatusCode().is2xxSuccessful()) {
            hasError = true;
        }
        return hasError;
    }

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
