package com.example.securitydemo.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static com.example.securitydemo.security.ApplicationUserRole.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Configuration
/* we can use @EnableWebFluxSecurity if we use webflux in this class config and add to pom webflux starter and look to https://www.baeldung.com/spring-security-5-reactive.
webflux security config is a little bit different from web-starter
 In this class we are going to define our security we must extends WebSecurityConfigurerAdapter and if we do ctrl + o we can see all the method we can override from extends class,
the one we will choose is configure with HttpSecurity param
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /**
                 * look csrf.png . csrf (cross side request forgery) that mean imitation or a false copy of a real document(bank signature etc..) or action. the attacker
                 * forge a request to take money from user that is log ia good site where you can by by your credit card articles, so the attacker will forge a request and embed into it
                 * a malicious link that if a user click on it sent money from user to attacker.
                 * The way spring security protect from that is by enable csrf look csrf_enable_protection.png :
                 * when the client frontend log in our server, spring security send a csrf token from server to client in a cookie
                 * a frontend use that token for every form submission (post, put, delete) the server validate that token to see if it match with what it have send previously.
                 *  we have disable csrf (.csrf().disable()) under to be able to do post, put and delete because we have no access to the token csrf send by our server and any
                 *  3 verb was failing but now we are going to generate that csrf token for our client to avoid to let .csrf().disable() which is a very very bad for security
                 *  when aur client submit directly request to our server in that case we must replace .csrf().disable() by .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                 *  and we have to implement in our client how to get that token and send it back in the header of our request (https://www.baeldung.com/spring-security-csrf)
                 *  but if our end point are not directly call by user but it is just internal calling we can left .csrf().disable() but in the case of interaction between user request
                 *  and server never forget to not disable csrf and implement token get by client and sent in post, put delete verb.
                 *  For the seek of our tutorial and the fact that we test with postman we can disable it
                 *
                 */
                .csrf().disable()
                .authorizeRequests() // we want to authorize request
                /* The next both line is to tell that any file that will be in root(/), will have index in the name, will be in /css and /js
                dir dont need to be authenticate. i can test on browser by this url http://localhost:7902/ (it will give me access to index.html define in resources/static),
                if i comment the both lines and i access again to the url http://localhost:7902/ (refresh page) a popup with username and password will be ask on to me
                * */
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name()) // we say that only ROLE_STUDENT will be only to access any thing with pattern /api/***
                /**
                 * only users with COURSE_WRITE permissions can delete, post and put in management api
                 * and also we add more security by saying that management api can only be access by roles ADMIN and ADMINTRAINEE
                 */
                //.antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission()) // tom will not be able to delete in this api because ADMINTRAINEE not have that permission
                //.antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                //.antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                //.antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name()) // the 2 roles can only access to pattern "/management/api/**" with GET verb
                /**
                 * i can comment the line up and remove verb but becarefull because i have to respect order of matchers because for tom it will ask:
                 * can i access /api/** : no tom has no STUDENT role
                 * can i delete on /management/api/** (when tom try) no because tom not have COURSE_WRITE permission, the same thing for Post and Put
                 * but if we put .antMatchers("/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name()) Delete, post and put matcher since we have not
                 * put the verb, tom will be able to delete post and put so order is important and the better way is to put a verb to be more secure
                 */
                //.antMatchers("/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
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
                /**
                 * Step 4
                 * when we click on roles under we go to the function that add a role in param by appending it with ROLE_ in the list of authorities after instantiation
                 * GrantedAuthority or SimpleGrantedAuthority which implement GrantedAuthority. so we can replace roles function by building our own collections of SimpleGrantedAuthority.
                 * Since ours roles are define in ApplicationUserRole.class we are going to build our collection of SimpleGrantedAuthority in grantedAuthorities function
                 * we now comment .roles(STUDENT.name()) and call our function STUDENT.grantedAuthorities()) by using .authorities() witch take collection of GrantedAuthority
                 * So for each user by using  authorities() we attach all his permissions and his role inside collection of GrantedAuthority
                 */
                //.roles(STUDENT.name()) // role_name STUDENT will be write internally by spring security as ROLE_STUDENT
                .authorities(STUDENT.grantedAuthorities())
                .build();

        UserDetails linda = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("mypassword123")) // in api we must force user to have a strong password to avoid brut force
                //.roles(ADMIN.name()) // role_name ADMIN will be write internally by spring security as ROLE_ADMIN
                .authorities(ADMIN.grantedAuthorities())
                .build();

        UserDetails tom = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("mypassword1234")) // in api we must force user to have a strong password to avoid brut force
                //.roles(ADMINTRAINEE.name()) // role_name ADMIN will be write internally by spring security as ROLE_ADMINTRAINEE
                .authorities(ADMINTRAINEE.grantedAuthorities())
                .build();
        return new InMemoryUserDetailsManager(annaSmith, linda, tom);

    }
}
