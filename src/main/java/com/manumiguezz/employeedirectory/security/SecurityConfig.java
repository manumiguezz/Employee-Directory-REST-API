package com.manumiguezz.employeedirectory.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        PathPatternRequestMatcher.Builder pathMatcher = PathPatternRequestMatcher.withDefaults();

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(pathMatcher.matcher(HttpMethod.GET, "/api/employees")).hasRole("EMPLOYEE")
                        .requestMatchers(pathMatcher.matcher(HttpMethod.GET, "/api/employees/{employeeId}")).hasRole("EMPLOYEE")
                        .requestMatchers(pathMatcher.matcher(HttpMethod.POST, "/api/employees")).hasRole("MANAGER")
                        .requestMatchers(pathMatcher.matcher(HttpMethod.PUT, "/api/employees/{employeeId}")).hasRole("MANAGER")
                        .requestMatchers(pathMatcher.matcher(HttpMethod.DELETE, "/api/employees/{employeeId}")).hasRole("ADMIN")
                        .anyRequest().authenticated()
        );

        http.httpBasic(Customizer.withDefaults());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
