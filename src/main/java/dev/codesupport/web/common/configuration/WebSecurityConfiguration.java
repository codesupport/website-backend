package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.security.HttpRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures security options
 * <p>Sets up security configurations including:
 * Password Encryption Type,
 * Authentication Provider,
 * Request Filters</p>
 */
//unused - This is called dynamically by springboot framework
@SuppressWarnings("unused")
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private SimpleAuthenticationEntryPoint simpleAuthenticationEntryPoint;
    private HttpRequestFilter httpRequestFilter;

    @Autowired
    public WebSecurityConfiguration(
            SimpleAuthenticationEntryPoint simpleAuthenticationEntryPoint,
            HttpRequestFilter httpRequestFilter
    ) {
        this.simpleAuthenticationEntryPoint = simpleAuthenticationEntryPoint;
        this.httpRequestFilter = httpRequestFilter;
    }

    /**
     * Configure HttpSecurity
     * <p>Sets up configurations for the HttpSecurity such as CSRF and authentication.</p>
     *
     * @param httpSecurity The HttpSecurity to configure
     * @throws Exception Thrown by framework
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable() //CSRF disabled for now
                .headers().frameOptions().disable()
                .and()
                // Set entry point for authentication, this seems to only fire on auth failures?
                .exceptionHandling().authenticationEntryPoint(simpleAuthenticationEntryPoint).and()
                // Set up for stateless
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add JWT filter to apply to incoming requests.
        httpSecurity.addFilterBefore(httpRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}