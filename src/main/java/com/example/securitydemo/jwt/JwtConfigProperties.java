package com.example.securitydemo.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;

//add dependencies spring-boot-configuration-processor in pom
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
//add dependencies spring-boot-configuration-processor in pom and EnableConfigurationProperties
@ConfigurationProperties("application.jwt")
public class JwtConfigProperties {
    String secretKey;
    String tokenPrefix;
    Integer tokenExpirationAfterDays;

    // look on http header of postman we will seek this key value
    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
