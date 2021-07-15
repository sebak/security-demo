package com.example.securitydemo.jwt;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
public class UsernamePasswordAuthenticationRequest {

    String username;
    String password;

}
