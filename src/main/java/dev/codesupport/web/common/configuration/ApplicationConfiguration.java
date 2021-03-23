package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.service.http.client.ObjectToUrlEncodedConverter;
import dev.codesupport.web.common.service.http.client.RestTemplateResponseErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * Configures the application.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    /**
     * @return The type of password encryption to use
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Offers various configurations for the http requests.
     *
     * @return A ClientHttpRequestFactory
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);

        return new BufferingClientHttpRequestFactory(factory);
    }

    /**
     * Configures Spring's RestTemplate client
     *
     * @param factory The ClientHttpRequestFactory to use with the client
     * @return The configured RestTemplate client.
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);

        // Add message converters - Ex: converting to urlencoded messages
        restTemplate.getMessageConverters()
                .add(new ObjectToUrlEncodedConverter());

        // Set a handler to decide if and how errors are handled.
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());

        return restTemplate;
    }

}