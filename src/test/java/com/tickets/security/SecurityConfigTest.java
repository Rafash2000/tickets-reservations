package com.tickets.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Test
    public void userDetailsServiceShouldBeConfigured() {
        assertThat(userDetailsService).isNotNull();
    }

    @Test
    public void passwordEncoderShouldBeBCrypt() {
        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    public void securityFilterChainShouldBeConfigured() {
        assertThat(securityFilterChain).isNotNull();
    }

    @Test
    public void passwordEncoderShouldMatchPassword() {
        String rawPassword = "testPassword";
        String encodedPassword = securityConfig.passwordEncoder().encode(rawPassword);

        assertThat(securityConfig.passwordEncoder().matches(rawPassword, encodedPassword)).isTrue();
    }
}
