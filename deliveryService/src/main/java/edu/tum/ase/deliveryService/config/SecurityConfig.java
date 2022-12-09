package edu.tum.ase.deliveryService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // Note: for demonstration purposes only, this should not be done
                .cors().disable()
                .authorizeRequests() // 2. Require authentication in all endpoints except login
                .anyRequest().permitAll()
                .and().httpBasic();
    }
}
