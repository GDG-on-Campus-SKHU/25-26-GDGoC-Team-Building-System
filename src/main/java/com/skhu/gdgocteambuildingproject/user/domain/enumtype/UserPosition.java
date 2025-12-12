package com.skhu.gdgocteambuildingproject.user.domain.enumtype;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;

public enum UserPosition {
    MEMBER, CORE, ORGANIZER;

    @JsonCreator
    public static UserPosition from(String value) {
        try {
            return UserPosition.valueOf(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(
                    ExceptionMessage.INVALID_USER_POSITION.getMessage()
            );
        }
    }
}
