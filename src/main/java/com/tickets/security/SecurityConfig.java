package com.tickets.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Value("${spring.security.admin.name}")
    private String username;

    @Value("${spring.security.admin.password}")
    private String password;

    @Value("${spring.security.admin.role}")
    private String role;

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername(username).password(passwordEncoder().encode(password)).roles(role).build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(new AntPathRequestMatcher("/seats")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/wagons")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/trains")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/timetables")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/reservations")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/reservations/{id}")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/reservations/startAndEndStation/{startStation}/{endStation}/{startTime}/seatType/{seatType}")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/reservations/startAndEndStation/{startStation}/{endStation}/{startTime}/numberOfSeats/{numberOfSeats}")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/reservations/startAndEndStation/{startStation}/{endStation}")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/reservations/endStations/{startStation}")).permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
