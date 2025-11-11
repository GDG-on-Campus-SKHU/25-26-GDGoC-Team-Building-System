package com.skhu.gdgocteambuildingproject.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    // TeamBuilding
    SCHEDULE_NOT_EXIST("일정이 존재하지 않습니다."),
    PROJECT_NOT_EXIST("프로젝트가 존재하지 않습니다."),

    USER_NOT_FOUND("해당 ID의 회원을 찾을 수 없습니다."),
    USER_ALREADY_APPROVED("이미 승인된 회원입니다."),
    USER_ALREADY_REJECTED("이미 거절된 회원입니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}
