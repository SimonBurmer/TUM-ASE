package edu.tum.ase.emailservice.config;

import edu.tum.ase.backendCommon.config.CustomWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends CustomWebSecurityConfigurerAdapter {}
