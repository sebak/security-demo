package com.example.securitydemo.api;

import lombok.experimental.UtilityClass;

/**
 *  Root manage api root and version if we need many version
 */
@UtilityClass // no need to be instantiate as class
public class RootPaths {

    static final String BASE_API_PATH = "/api";
    public static  final String BASE_API_V1_PATH = BASE_API_PATH + "/v1";
}
