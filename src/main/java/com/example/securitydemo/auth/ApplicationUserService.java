package com.example.securitydemo.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 step 9 look db_1_authentication.png and db_2_auth_with_user_and_service.png
 */
@RequiredArgsConstructor
@Service
public class ApplicationUserService implements UserDetailsService {

    /**
     * in case we have many implementation of ApplicationUserDao qualifier is use to choose witch one we instantiate here it is FakeApplicationUserService one.
     * if we just have one class that implement ApplicationUserDao there is not confusion for spring boot, so we have not oblige to add @Qualifier annotation
     */
    @Qualifier("fake")
    private final ApplicationUserDao applicationUserDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserDao.selectApplicationUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
    }
}
