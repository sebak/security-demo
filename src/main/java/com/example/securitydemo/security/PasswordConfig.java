package com.example.securitydemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    /*
        PasswordEncoder is an interface (click to it) it define 3 methods (we will use encode one)  and we can see the classes that implements that interface and one of them is
        BCryptPasswordEncoder that we choose to use and we will inject it in ApplicationSecurityConfig to be able to call encode function
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
