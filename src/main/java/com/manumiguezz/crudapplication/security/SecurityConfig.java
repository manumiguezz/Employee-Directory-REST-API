package com.manumiguezz.crudapplication.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/employees")).hasRole("EMPLOYEE")
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/employees/**")).hasRole("EMPLOYEE")
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/employees")).hasRole("MANAGER")
                        .requestMatchers(antMatcher(HttpMethod.PUT, "/api/employees/**")).hasRole("MANAGER")
                        .requestMatchers(antMatcher(HttpMethod.DELETE, "/api/employees/**")).hasRole("ADMIN")
                        .anyRequest().authenticated()
        );

        http.httpBasic(Customizer.withDefaults());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
