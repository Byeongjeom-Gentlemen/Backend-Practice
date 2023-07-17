package com.sh.global.util;

import lombok.Getter;

@Getter
public enum UserRole {

    ROLE_USER("ROLE_USER"), ROLE_ADMIN("ROLE_ADMIN");

    private String description;

    UserRole(String description) {
        this.description = description;
    }
}
