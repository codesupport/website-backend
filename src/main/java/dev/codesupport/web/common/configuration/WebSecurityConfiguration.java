package dev.codesupport.web.common.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Configures security options
 */
//unused - This is called dynamically by springboot framework
@SuppressWarnings("unused")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final HttpSessionProperties httpSessionProperties;

    /**
     * Configure HttpSecurity
     * <p>Sets up configurations for the HttpSecurity such as CSRF and authentication.</p>
     *
     * @param httpSecurity The HttpSecurity to configure
     * @throws Exception Thrown by framework
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().frameOptions().disable()
                .and().csrf().disable() //CSRF disabled for now
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

}