package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.exception.HttpRequestException;
import dev.codesupport.web.common.security.models.DiscordOAuthTokenRequest;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.common.service.service.ObjectToUrlEncodedConverter;
import dev.codesupport.web.common.service.service.RequestResponseLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Configures the application.
 */
@Configuration
class ApplicationConfiguration {

    /**
     * Performs configurations requires for the application to function.
     * <p>This is automatically found and executed by Spring.</p>
     *
     * @param context Spring's ApplicationContext
     */
    @Autowired
    ApplicationConfiguration(
            ApplicationContext context,
            DiscordAppConfiguration discordAppConfiguration
    ) {
        CrudOperations.setContext(context);
        if (discordAppConfiguration.isValid()) {
            DiscordOAuthTokenRequest.setClient_id(discordAppConfiguration.getClientId());
            DiscordOAuthTokenRequest.setSecret(discordAppConfiguration.getSecret());
            DiscordOAuthTokenRequest.setRedirect_uri(discordAppConfiguration.getRedirectUri());
        } else {
            throw new ConfigurationException("Discord authorization not configured.");
        }
    }

    /**
     * @return The type of password encryption to use
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        //TODO: This needs cleanup before it's finalized.
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());

        RestTemplate restTemplate = new RestTemplate(factory);

        restTemplate.getMessageConverters()
                .add(new ObjectToUrlEncodedConverter());

        restTemplate.getInterceptors()
                .add(new RequestResponseLoggingInterceptor());

        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                boolean hasError = false;
                if (!HttpStatus.OK.equals(clientHttpResponse.getStatusCode())) {
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
        });

        return restTemplate;
    }

}