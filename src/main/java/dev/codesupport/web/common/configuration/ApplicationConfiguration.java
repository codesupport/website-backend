package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.security.models.DiscordOAuthTokenRequest;
import dev.codesupport.web.common.service.http.ObjectToUrlEncodedConverter;
import dev.codesupport.web.common.service.http.RestTemplateResponseErrorHandler;
import dev.codesupport.web.common.service.service.CrudOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
            DiscordAppProperties discordAppProperties
    ) {
        // Set ApplicationContext for all CrudOperation instances
        CrudOperations.setContext(context);

        // Set discord app properties as static (default) values of DiscordOAuthTokenRequest
        DiscordOAuthTokenRequest.setClient_id(discordAppProperties.getClientId());
        DiscordOAuthTokenRequest.setSecret(discordAppProperties.getSecret());
        DiscordOAuthTokenRequest.setRedirect_uri(discordAppProperties.getRedirectUri());
    }

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