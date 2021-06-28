package com.example.securitydemo.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.securitydemo.security.ApplicationUserRole.*;

@RequiredArgsConstructor
@Repository("fake") //  parameter is a name of this component in case we have other implementation of ApplicationUserDao
public class FakeApplicationUserService implements ApplicationUserDao {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUser()
                .stream()
                .filter(applicationUser -> applicationUser.getUsername().equals(username))
                .findFirst();
    }

    // it is a kind of mock of what we can get from database
    private List<ApplicationUser> getApplicationUser() {
        List<ApplicationUser> applicationUsers = List.of(
                new ApplicationUser(STUDENT.grantedAuthorities(), "mypassword", "annasmith", true, true, true, true),
                new ApplicationUser(ADMIN.grantedAuthorities(), "mypassword123", "linda", true, true, true, true),
                new ApplicationUser(ADMINTRAINEE.grantedAuthorities(), "mypassword1234", "tom", true, true, true, true)
        );

        return applicationUsers;
    }
}
