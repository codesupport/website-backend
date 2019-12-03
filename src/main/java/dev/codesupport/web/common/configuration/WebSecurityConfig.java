package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures security options
 * <p>Sets up security configurations including:
 * Password Encryption Type,
 * Authentication Provider,
 * Request Filters</p>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)//(prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private SimpleAuthenticationEntryPoint simpleAuthenticationEntryPoint;
    private UserDetailsService userDetailsService;
    private JwtRequestFilter jwtRequestFilter;
    private AuthenticationProvider authenticationProvider;

    @Autowired
    public WebSecurityConfig(
            SimpleAuthenticationEntryPoint simpleAuthenticationEntryPoint,
            UserDetailsService userDetailsService,
            JwtRequestFilter jwtRequestFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.simpleAuthenticationEntryPoint = simpleAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * @return The type of password encryption to use
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Returns the configured authentication manager
     *
     * @return The configured AuthenticationManager
     * @throws Exception Thrown by framework
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean(); // Default configuration
    }

    /**
     * Configure the AuthenticationManagerBuilder
     * <p>Configures the AuthenticationManagerBuilder to be used.</p>
     *
     * @param auth AuthenticationManagerBuilder to configure
     * @throws Exception Thrown by framework
     */
    @Autowired
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(authenticationProvider) // Configure authentication Provider to use
                .userDetailsService(userDetailsService) // Configure UserDetailService to use
                .passwordEncoder(passwordEncoder()); // Configure encoder to use
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
                // Set entry point for authentication, this seems to only fire on auth failures?
                .exceptionHandling().authenticationEntryPoint(simpleAuthenticationEntryPoint).and()
                // Set up for stateless
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add JWT filter to apply to incoming requests.
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}