package com.example.securitydemo.security;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.example.securitydemo.security.ApplicationUserPermission.*;

//a role can have 0 or n permissions
public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()), // student in this case has no permissions
    ADMIN(Sets.newHashSet(STUDENT_READ, STUDENT_WRITE, COURSE_READ, COURSE_WRITE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }
}
