package com.skhu.gdgocteambuildingproject.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    // Common
    USER_NOT_EXIST("회원이 존재하지 않습니다."),
    USER_NOT_FOUND("해당 ID의 회원을 찾을 수 없습니다."),
    FILE_NOT_EXIST("파일이 존재하지 않습니다."),
    BANNED_USER("정지된 계정입니다."),
    ALREADY_BANNED_USER("이미 정지된 계정입니다."),
    ALREADY_ACTIVE_USER("이미 활성화된 계정입니다."),

    // TeamBuilding
    SCHEDULE_NOT_EXIST("일정이 존재하지 않습니다."),
    NOT_REGISTRATION_SCHEDULE("아이디어 등록 기간이 아닙니다."),
    SCHEDULE_PASSED("일정이 지났습니다."),
    ILLEGAL_SCHEDULE_DATE("일정의 날짜가 부적절합니다."),
    ENROLLMENT_NOT_EXIST("지원이 존재하지 않습니다."),
    ILLEGAL_ENROLLMENT_STATUS("지원의 상태가 부적절합니다."),
    ENROLLMENT_FOR_OTHER_IDEA("다른 아이디어에 대한 지원입니다."),
    ENROLLMENT_NOT_AVAILABLE("지원할 수 없는 상태입니다."),
    IDEA_CREATOR_CANNOT_ENROLL("아이디어를 게시한 회원은 다른 아이디어에 지원할 수 없습니다"),
    ALREADY_ENROLL("해당 아이디어에 이미 지원했습니다."),
    PROJECT_NOT_EXIST("프로젝트가 존재하지 않습니다."),
    IDEA_NOT_EXIST("아이디어가 존재하지 않습니다."),
    NOT_MEMBER_OF_IDEA("해당 아이디어에 소속되지 않았습니다"),
    REGISTERED_IDEA_NOT_EXIST("등록된 아이디어가 존재하지 않습니다."),
    REGISTERED_IDEA_ALREADY_EXIST("등록한 아이디어가 이미 존재합니다."),
    IDEA_CONTENTS_EMPTY("내용이 비어 있습니다."),
    TEMPORARY_IDEA_NOT_EXIST("임시 저장된 아이디어가 없습니다."),
    NOT_CREATOR_OF_IDEA("아이디어 게시자가 아닙니다."),
    CREATOR_NOT_INIT("아이디어 게시자에 대한 정보가 초기화되지 않았습니다."),
    CHOICE_NOT_AVAILABLE("해당 지망을 사용할 수 없습니다."),
    PART_NOT_AVAILABLE("해당 프로젝트에서 모집하지 않는 파트입니다."),

    // Admin
    USER_ALREADY_APPROVED("이미 승인된 회원입니다."),
    USER_ALREADY_REJECTED("이미 거절된 회원입니다."),
    SCHEDULE_ALREADY_INITIALIZED("일정이 이미 초기화 되어 있습니다."),
    ACTIVITY_POST_NOT_FOUND("게시글을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND("카테고리를 찾을 수 없습니다."),
    USER_NOT_REJECTED("현재 상태가 REJECTED인 사용자만 재검토할 수 있습니다."),

    // GalleryProject
    PROJECT_NOT_EXIST_IN_GALLERY("프로젝트가 갤러리에 존재하지 않습니다."),
    PROJECT_LIST_NOT_EXIST_IN_GALLERY("아직 등록된 프로젝트가 없습니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}
