package com.example.practice.config.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PermissionEnum {
    CATEGORY_WRITE("category:write"),
    CATEGORY_READ("category:read"),
    CATEGORY_DELETE("category:delete");

    private String description;
}
