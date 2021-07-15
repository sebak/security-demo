package com.example.securitydemo.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@RequiredArgsConstructor
@Configuration
public class JwtSecretKey {
    private final JwtConfigProperties jwtConfigProperties;

    @Bean
    public SecretKey getSecretKeyForSigning() {
        return Keys.hmacShaKeyFor(jwtConfigProperties.getSecretKey().getBytes());
    }
}
