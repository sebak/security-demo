package com.example.securitydemo.auth;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor

/**
 * step 9 look db_1_authentication.png and db_2_auth_with_user_and_service.png
 * This class is use to build our own user by implementing UserDetail instead of using UserDetails builder as we done in in previous step by returning and in memory db of user
 */
public class ApplicationUser implements UserDetails {

    final Set<? extends GrantedAuthority> authorities;
    final String password;
    final String username;
    final boolean isAccountNonExpired;
    final boolean isAccountNonLocked;
    final boolean isCredentialsNonExpired;
    final boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
