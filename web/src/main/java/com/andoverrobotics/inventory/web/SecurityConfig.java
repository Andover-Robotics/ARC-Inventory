package com.andoverrobotics.inventory.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .httpBasic().disable()  // Disable default login page at /login and automatic redirects to /login
                .csrf().disable()       // Disable the need for csrf in ajax
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout-cookies")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }
}
