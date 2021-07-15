package com.example.securitydemo.jwt;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthorisationConstant {
    final static String AUTHORIZATION_KEY_HEADER = "Authorization";
    final static String AUTHORIZATION_PREFIX_VALUE_HEADER_FOR_JWT = "Bearer "; // must have a space, it will be concat with token
    final static String AUTHORITIES_KEY = "authorities";
    //final static String SECRET_KEY = "SecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecure!?!!!!!MUST_Be_Too_Long_AND_VERY_STRONG_TO_Be_BREAK_1234567890l";
}
