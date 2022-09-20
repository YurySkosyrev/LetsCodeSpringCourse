package com.example.sweeter.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * перечисление ролей пользователя
 * return name() - строковое представление значений Role
 */

public enum Role implements GrantedAuthority {
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
