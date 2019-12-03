package dev.codesupport.web.common.configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Authentication Implementation used for authorizations
 * <p>Authenticates the provided authentication credentials with Firebase</p>
 */
@Component
public class FirebaseAuthenticationProvider implements AuthenticationProvider {

    /**
     * Boilerplate, determines what authentication types this provider supports.
     *
     * @param authentication Authentication class type
     * @return True if the AuthenticationProvider supports it, False otherwise
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    /**
     * Authenticates the given authentication credentials
     *
     * @param authentication Authentication details to authenticate
     * @return New authentication token if successful, null otherwise
     */
    @Override
    public Authentication authenticate(Authentication authentication) {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UsernamePasswordAuthenticationToken authenticationToken;

        if (shouldAuthenticateAgainstThirdPartySystem(username, password)) {

            // use the credentials
            // and authenticate against the third-party system
            authenticationToken = new UsernamePasswordAuthenticationToken(
                    username,
                    password,
                    new ArrayList<>()
            );
        } else {
            authenticationToken = null;
        }

        return authenticationToken;
    }

    /**
     * Authenticates the given credentials with Firebase
     * Stubbed for now.
     *
     * @param username Username for credentials.
     * @param password Password for credentials.
     * @return True if credentials are authenticated with Firebase, False otherwise.
     */
    boolean shouldAuthenticateAgainstThirdPartySystem(String username, String password) {
        //TODO: Federate authentication with Firebase.
        //Stubbed for now.
        return username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin");
    }

}
