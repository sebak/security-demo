package com.example.securitydemo.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.securitydemo.security.ApplicationUserPermission.*;

//a role can have 0 or n permissions
public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()), // student in this case has no permissions
    ADMIN(Sets.newHashSet(STUDENT_READ, STUDENT_WRITE, COURSE_READ, COURSE_WRITE)),
    ADMINTRAINEE(Sets.newHashSet(STUDENT_READ, COURSE_READ)); // the permissions of this role is define in step_3_acces_api_by_roles_permission.png

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    /**
     * step-4 any role here has in param a set of ApplicationUserPermission in the variable permissions, so in this function we create a set of SimpleGrantedAuthority
     * (SimpleGrantedAuthority implement GrantedAuthority) SimpleGrantedAuthority is instantiate by String param new SimpleGrantedAuthority("my string") but for us we are going to put
     * new SimpleGrantedAuthority(permission.getPermission()) (example: for STUDENT_READ ApplicationUserPermission the sting will be "student:read" new SimpleGrantedAuthority("student:read"))
     * We also add in set of SimpleGrantedAuthority the current role appending to ROLE_ as this new SimpleGrantedAuthority("ROLE_" + this.name())
     *
     * So the function grantedAuthorities for ADMINTRAINEE role will have a set of SimpleGrantedAuthority with :
     * SimpleGrantedAuthority("student:read"), SimpleGrantedAuthority("course:read"), SimpleGrantedAuthority("ROLE_ADMINTRAINEE")
     *
     *
     * Look explanation on ApplicationSecurityConfig.class on function .roles() of userDetailsService function
     *
     * @return Set of SimpleGrantedAuthority for a ApplicationUserRole
     */
    public Set<SimpleGrantedAuthority> grantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return  permissions;
    }
}
