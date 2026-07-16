package com.example.practice.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

import static com.example.practice.config.security.PermissionEnum.*;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    ADMIN(Set.of( CATEGORY_WRITE,  CATEGORY_READ)),
    USER(Set.of( CATEGORY_READ));

    private Set<PermissionEnum> permissions;
}
