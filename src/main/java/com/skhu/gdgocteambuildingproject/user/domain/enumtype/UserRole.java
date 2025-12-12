package com.skhu.gdgocteambuildingproject.user.domain.enumtype;

import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;

public enum UserRole {
    ROLE_SKHU_ADMIN, ROLE_SKHU_MEMBER, ROLE_OTHERS;

    public static UserRole from(String value) {
        try {
            return UserRole.valueOf(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(
                    ExceptionMessage.INVALID_USER_ROLE.getMessage()
            );
        }
    }
}
