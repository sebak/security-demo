package com.example.securitydemo.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.example.securitydemo.security.ApplicationUserRole.ADMIN;
import static com.example.securitydemo.security.ApplicationUserRole.STUDENT;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Configuration
/* we can use @EnableWebFluxSecurity if we use webflux in this class config and add to pom webflux starter and look to https://www.baeldung.com/spring-security-5-reactive.
webflux security config is a little bit different from web-starter
 In this class we are going to define our security we must extends WebSecurityConfigurerAdapter and if we do ctrl + o we can see all the method we can override from extends class,
the one we will choose is configure with HttpSecurity param
 */
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // we want to authorize request
                /* The next both line is to tell that any file that will be in root(/), will have index in the name, will be in /css and /js
                dir dont need to be authenticate. i can test on browser by this url http://localhost:7902/ (it will give me access to index.html define in resources/static),
                if i comment the both lines and i access again to the url http://localhost:7902/ (refresh page) a popup with username and password will be ask on to me
                * */
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name()) // we say that only ROLE_STUDENT will be only to access any thing with pattern /api/***
                .anyRequest() // here plus this line it become: we want to authorize any request
                .authenticated() // here plus this line it become: any request we want to authorize must be authenticated (client must specify username and password)
                .and()
                .httpBasic(); // here plus this line it become: any request we want to authorize must be authenticated with basic authentication
    }

    @Override
    @Bean // so that UserDetailsService may be instantiate for us in application loading
    /*
    UserDetailsService is an interface we can click to it and see all the class that implement it and in this case we want to return one of them that is
    InMemoryUserDetailsManager (that is the one that use user from memory database but in this case we will define our own username (paul) and our own password (mypassword)
    instead of default spring boot one with username: user and random uuid password generate on console when we start service. we can test our are new user and password
    http://localhost:7902/api/v1/student/eb8d7ad4-cd4f-11eb-b5f2-34e6d72a5caa
     */
    protected UserDetailsService userDetailsService() {
        UserDetails annaSmith = User.builder()
                .username("annasmith")
                /* if we just put it as .password("password") it will fail when we will try to connect in browser with Paul and password for all secure endpoint because we don't
                have passwordEncoder so we must generate a PasswordEncoder as bean by returning one of the class that implement that interface to be able to use one of the function
                (encode) define in that interface  so we do that in PasswordConfig class and use it here after instantiation*/
                .password(passwordEncoder.encode("mypassword")) // in api we must force user to have a strong password to avoid brut force
                .roles(STUDENT.name()) // role_name STUDENT will be write internally by spring security as ROLE_STUDENT
                .build();

        UserDetails linda = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("mypassword123")) // in api we must force user to have a strong password to avoid brut force
                .roles(ADMIN.name()) // role_name ADMIN will be write internally by spring security as ROLE_ADMIN
                .build();
        return new InMemoryUserDetailsManager(annaSmith, linda);

    }
}
