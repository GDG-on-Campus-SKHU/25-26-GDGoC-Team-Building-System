package com.skhu.gdgocteambuildingproject.user.domain.enumtype;

import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;

public enum UserRole {
    SKHU_ADMIN, SKHU_MEMBER, OTHERS;

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
