package com.ernyka.cardmanager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PassConfiguration {
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        if (this.passwordEncoder != null) {
            return this.passwordEncoder;
        } else
            return new BCryptPasswordEncoder();
    }
}
