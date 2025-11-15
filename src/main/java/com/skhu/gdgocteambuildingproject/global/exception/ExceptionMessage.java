package com.skhu.gdgocteambuildingproject.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    // Common
    USER_NOT_EXIST("회원이 존재하지 않습니다."),

    // TeamBuilding
    SCHEDULE_NOT_EXIST("일정이 존재하지 않습니다."),
    PROJECT_NOT_EXIST("프로젝트가 존재하지 않습니다."),

    USER_NOT_FOUND("해당 ID의 회원을 찾을 수 없습니다."),
    USER_ALREADY_APPROVED("이미 승인된 회원입니다."),
    USER_ALREADY_REJECTED("이미 거절된 회원입니다."),
  
    IDEA_NOT_EXIST("아이디어가 존재하지 않습니다."),
    REGISTERED_IDEA_ALREADY_EXIST("등록한 아이디어가 이미 존재합니다."),
    IDEA_CONTENTS_EMPTY("내용이 비어 있습니다."),

    // GalleryProject
    PROJECT_NOT_EXIST_IN_GALLERY("프로젝트가 갤러리에 존재하지 않습니다."),
    PROJECT_LIST_NOT_EXIST_IN_GALLERY("아직 등록된 프로젝트가 없습니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}
