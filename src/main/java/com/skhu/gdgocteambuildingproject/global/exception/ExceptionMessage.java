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

    //Auth
    EMAIL_ALREADY_EXISTS("이미 가입된 이메일입니다."),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
    USER_NOT_APPROVED("관리자 승인 대기 중입니다."),
    USER_REJECTED("승인이 거절된 사용자입니다."),
    REFRESH_TOKEN_INVALID("유효하지 않은 리프레시 토큰입니다."),
    INVALID_JWT_TOKEN("유효하지 않은 JWT 토큰입니다."),

    // Email
    USER_EMAIL_NOT_EXIST("존재하지 않거나 탈퇴한 이메일입니다."),
    EMAIL_INVALID_FORMAT("유효하지 않은 이메일 형식입니다."),
    VERIFICATION_CODE_EXPIRED("인증번호가 만료되었습니다."),
    VERIFICATION_CODE_INVALID("인증번호가 일치하지 않습니다."),
    PASSWORD_INVALID_FORMAT("비밀번호 형식이 올바르지 않습니다."),
    PASSWORD_SAME_AS_OLD("새 비밀번호는 기존 비밀번호와 달라야 합니다."),

    // TeamBuilding
    NOT_PARTICIPATED("프로젝트에 접근 권한이 없습니다."),
    ALREADY_PARTICIPATED("이미 프로젝트에 참여하고 있습니다."),
    SCHEDULE_NOT_EXIST("일정이 존재하지 않습니다."),
    NOT_REGISTRATION_SCHEDULE("아이디어 등록 기간이 아닙니다."),
    SCHEDULE_PASSED("일정이 지났습니다."),
    ILLEGAL_SCHEDULE_DATE("일정의 날짜가 부적절합니다."),
    ENROLLMENT_NOT_EXIST("지원이 존재하지 않습니다."),
    ILLEGAL_ENROLLMENT_STATUS("지원의 상태가 부적절합니다."),
    ENROLLMENT_FOR_OTHER_IDEA("다른 아이디어에 대한 지원입니다."),
    ENROLLMENT_BY_OTHER_USER("본인의 지원이 아닙니다."),
    ENROLLMENT_NOT_AVAILABLE("지원할 수 없는 상태입니다."),
    ENROLLMENT_NOT_CANCELABLE("지원을 취소할 수 없는 상태입니다."),
    IDEA_CREATOR_CANNOT_ENROLL("아이디어를 게시한 회원은 다른 아이디어에 지원할 수 없습니다"),
    IDEA_MEMBER_CANNOT_ENROLL("이미 다른 아이디어에 멤버로 소속된 회원은 다른 아이디어에 지원할 수 없습니다"),
    ALREADY_ENROLL("해당 아이디어에 이미 지원했습니다."),
    PROJECT_NOT_EXIST("프로젝트가 존재하지 않습니다."),
    ILLEGAL_PROJECT("잘못된 프로젝트입니다."),
    PROJECT_ALREADY_EXISTS("현재 진행중이거나 진행 예정인 프로젝트가 존재합니다."),
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
    IDEA_TOTAL_MEMBER_COUNT_EXCEEDED("아이디어의 총 멤버 구성 인원수가 프로젝트의 최대 인원수를 초과합니다."),
    MAX_MEMBER_COUNT_TOO_SMALL("최대 인원수는 현재 인원수(수락 + 수락 예정)보다 작을 수 없습니다."),
    IDEA_MEMBER_NOT_FOUND("해당 ID의 아이디어멤버를 찾울 수 없습니다"),

    // Admin
    USER_ALREADY_APPROVED("이미 승인된 회원입니다."),
    USER_ALREADY_REJECTED("이미 거절된 회원입니다."),
    SCHEDULE_ALREADY_INITIALIZED("일정이 이미 초기화 되어 있습니다."),
    ACTIVITY_POST_NOT_FOUND("게시글을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND("카테고리를 찾을 수 없습니다."),
    USER_NOT_REJECTED("현재 상태가 REJECTED인 사용자만 재검토할 수 있습니다."),
    GENERATION_NOT_FOUND("해당 활동 기수를 찾을 수 없습니다."),
    INVALID_GENERATION("잘못된 Generation 값입니다."),

    // GalleryProject
    PROJECT_NOT_EXIST_IN_GALLERY("프로젝트가 갤러리에 존재하지 않습니다."),
    PROJECT_LIST_NOT_EXIST_IN_GALLERY("아직 등록된 프로젝트가 없습니다."),

    // S3
    IO_EXCEPTION_ON_IMAGE_UPLOAD("이미지 업로드 과정에서 에외가 발생했습니다."),
    IO_EXCEPTION_ON_IMAGE_DELETE("이미지 삭제 과정에서 예외가 발생했습니다."),
    EMPTY_FILE("파일이 비어있거나 파일 이름이 비어있습니다."),
    NO_FILE_NAME("올바르지 않은 파일의 이름입니다."),
    NO_FILE_EXTENSION("파일 확장자가 없습니다."),
    INVALID_FILE_EXTENSION("지원하지 않는 확장자입니다."),
    PUT_OBJECT_EXCEPTION("S3 버킷에 이미지를 업로드하는 과정에서 예외가 발생했습니다."),
    FILE_UPLOAD_TRANSACTION_FAILED("데이터베이스에 파일 저장 실패, S3에 업로드된 파일을 삭제합니다."),

    // Scheduler
    SCHEDULE_CONFIRM_FAILED("일정을 처리하던 중 예외가 발생했습니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}
