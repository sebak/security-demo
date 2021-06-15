package com.example.securitydemo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
/* we can use @EnableWebFluxSecurity if we use webflux in this class config and add to pom webflux starter and look to https://www.baeldung.com/spring-security-5-reactive.
webflux security config is a little bit different from web-starter
 In this class we are going to define our security we must extends WebSecurityConfigurerAdapter and if we do ctrl + o we can see all the method we can override from extends class,
the one we will choose is configure with HttpSecurity param
 */
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // we want to authorize request
                /* The next both line is to tell that any file that will be in root(/), will have index in the name, will be in /css and /js
                dir dont need to be authenticate. i can test on browser by this url http://localhost:7902/ (it will give me access to index.html define in resources/static),
                if i comment the both lines and i access again to the url http://localhost:7902/ (refresh page) a popup with username and password will be ask on to me
                * */
                .antMatchers("/", "index", "/css/*", "/js/*")
                .permitAll()
                .anyRequest() // here plus this line it become: we want to authorize any request
                .authenticated() // here plus this line it become: any request we want to authorize must be authenticated (client must specify username and password)
                .and()
                .httpBasic(); // here plus this line it become: any request we want to authorize must be authenticated with basic authentication
    }
}
