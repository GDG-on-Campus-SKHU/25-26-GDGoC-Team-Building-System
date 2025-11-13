package com.skhu.gdgocteambuildingproject.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    // TeamBuilding
    SCHEDULE_NOT_EXIST("일정이 존재하지 않습니다."),
    PROJECT_NOT_EXIST("프로젝트가 존재하지 않습니다."),
    IDEA_NOT_EXIST("아이디어가 존재하지 않습니다."),

    // GalleryProject
    PROJECT_NOT_EXIST_IN_GALLERY("프로젝트가 갤러리에 존재하지 않습니다."),
    PROJECT_LIST_NOT_EXIST_IN_GALLERY("아직 등록된 프로젝트가 없습니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}