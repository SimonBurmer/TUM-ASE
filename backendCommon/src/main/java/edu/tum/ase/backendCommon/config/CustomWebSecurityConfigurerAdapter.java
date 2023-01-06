package edu.tum.ase.backendCommon.config;

import edu.tum.ase.backendCommon.filter.AuthRequestFilter;
import edu.tum.ase.backendCommon.filter.BearerRequestFilter;
import edu.tum.ase.backendCommon.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    // This should be fine, since CORS is done in the gateway
    //@Bean
    //CorsConfigurationSource corsConfigurationSource() {
    //    CorsConfiguration configuration = new CorsConfiguration();
    //    configuration.setAllowedOrigins(List.of("http://localhost:3000")); // TODO: set correct value here
    //    configuration.setAllowedMethods(List.of("*"));
    //    configuration.setAllowedHeaders(List.of("*"));
    //    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //    source.registerCorsConfiguration("/**", configuration);
    //    return source;
    //}

    @Autowired
    JwtUtil jwtUtil;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors().disable() // This should be fine, since CORS is done by the gateway
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and().addFilterBefore(new AuthRequestFilter(jwtUtil), AnonymousAuthenticationFilter.class)
                .addFilterAfter(new BearerRequestFilter(jwtUtil), AuthRequestFilter.class);

        http.authorizeRequests()
                .antMatchers("/auth/bearer").authenticated()
                .antMatchers("/auth/**").permitAll(); // Used to acquire jwt auth token

        http.authorizeRequests()
                // TODO: add endpoints that can be called by the frontend here
                .antMatchers("/box/**").authenticated()
                .antMatchers("/delivery/**").authenticated();

        http.authorizeRequests()
                // TODO: add endpoints that can be called by the boxClient here
                .antMatchers("/client/**").authenticated();
    }
}
