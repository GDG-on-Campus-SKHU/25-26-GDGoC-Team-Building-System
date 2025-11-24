package com.skhu.gdgocteambuildingproject.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    // Common
    USER_NOT_EXIST("회원이 존재하지 않습니다."),
    USER_NOT_FOUND("해당 ID의 회원을 찾을 수 없습니다."),
    FILE_NOT_EXIST("파일이 존재하지 않습니다."),

    // TeamBuilding
    SCHEDULE_NOT_EXIST("일정이 존재하지 않습니다."),
    ILLEGAL_SCHEDULE_DATE("일정의 날짜가 부적절합니다."),
    ENROLLMENT_NOT_AVAILABLE("지원할 수 없는 상태입니다."),
    PROJECT_NOT_EXIST("프로젝트가 존재하지 않습니다."),
    IDEA_NOT_EXIST("아이디어가 존재하지 않습니다."),
    REGISTERED_IDEA_ALREADY_EXIST("등록한 아이디어가 이미 존재합니다."),
    IDEA_CONTENTS_EMPTY("내용이 비어 있습니다."),
    TEMPORARY_IDEA_NOT_EXIST("임시 저장된 아이디어가 없습니다."),

    // Admin
    USER_ALREADY_APPROVED("이미 승인된 회원입니다."),
    USER_ALREADY_REJECTED("이미 거절된 회원입니다."),
    SCHEDULE_ALREADY_INITIALIZED("일정이 이미 초기화 되어 있습니다."),

    // GalleryProject
    PROJECT_NOT_EXIST_IN_GALLERY("프로젝트가 갤러리에 존재하지 않습니다."),
    PROJECT_LIST_NOT_EXIST_IN_GALLERY("아직 등록된 프로젝트가 없습니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}
