package com.skhu.gdgocteambuildingproject.admin.api;

import com.skhu.gdgocteambuildingproject.admin.dto.ApproveUserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.UserBanRequestDto;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "관리자의 승인된 멤버 관리 API", description = "관리자의 승인된 멤버 관리 API입니다.")
public interface AdminUserProfileApi {

    String DEFAULT_PAGE = "0";
    String DEFAULT_SIZE = "20";
    String DEFAULT_SORT_BY = "id";
    String DEFAULT_ORDER = "ASC";

    @Operation(summary = "승인된 유저 목록 조회", description = "관리자가 승인된 유저들의 목록을 페이징하여 조회합니다.")
    ResponseEntity<ApproveUserInfoPageResponseDto> getApproveUsers(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = DEFAULT_PAGE) int page,
            @Parameter(description = "페이지 크기", example = DEFAULT_SIZE) int size,
            @Parameter(description = "정렬 기준 필드", example = DEFAULT_SORT_BY) String sortBy,
            @Parameter(description = "정렬 방향 (ASC/DESC)", example = DEFAULT_ORDER) SortOrder order
    );

    @Operation(summary = "유저 정지 (Ban)", description = "관리자가 특정 유저를 정지하고, 정지 사유를 입력합니다.")
    ResponseEntity<Void> banUser(
            @Parameter(description = "정지할 유저 ID", example = "1", required = true) Long userId,
            UserBanRequestDto userBanRequestDto
    );

    @Operation(summary = "유저 정지 해제 (Unban)", description = "관리자가 특정 유저의 정지를 해제합니다.")
    ResponseEntity<Void> unbanUser(
            @Parameter(description = "정지 해제할 유저 ID", example = "1", required = true) Long userId
    );

    @Operation(
            summary = "승인 완료된 사용자 검색(이름)",
            description = """
                    관리자 페이지에서 승인 절차를 통과한 사용자들 중,
                    이름 기준으로 검색하여 조회합니다.
                    가입 신청 후 관리자 승인을 받은 사용자만 검색 대상으로 포함됩니다.
                    """
    )
    ResponseEntity<ApproveUserInfoPageResponseDto> searchUsersByName(
            @Parameter(description = "검색할 이름", example = "홍길동") String name,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = DEFAULT_PAGE) int page,
            @Parameter(description = "페이지 크기", example = DEFAULT_SIZE) int size,
            @Parameter(description = "정렬 기준 필드", example = DEFAULT_SORT_BY) String sortBy,
            @Parameter(description = "정렬 방향 (ASC/DESC)", example = DEFAULT_ORDER) SortOrder order
    );

    @Operation(
            summary = "승인 완료된 사용자 검색(파트)",
            description = """
                    관리자 페이지에서 승인 절차를 통과한 사용자들 중,
                    역할/파트(PM, BACKEND 등) 기준으로 검색하여 조회합니다.
                    가입 신청 후 관리자 승인된 사용자만 검색됩니다.
                    """
    )
    ResponseEntity<ApproveUserInfoPageResponseDto> searchUsersByPart(
            @Parameter(description = "검색할 파트", example = "BACKEND") Part part,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = DEFAULT_PAGE) int page,
            @Parameter(description = "페이지 크기", example = DEFAULT_SIZE) int size,
            @Parameter(description = "정렬 기준 필드", example = DEFAULT_SORT_BY) String sortBy,
            @Parameter(description = "정렬 방향 (ASC/DESC)", example = DEFAULT_ORDER) SortOrder order
    );

    @Operation(
            summary = "승인 완료된 사용자 검색(학교)",
            description = """
                    관리자 페이지에서 승인 절차를 통과한 사용자들 중,
                    학교명 기준으로 검색하여 조회합니다.
                    가입 신청 후 관리자 승인을 받은 사용자만 검색 대상에 포함됩니다.
                    """
    )
    ResponseEntity<ApproveUserInfoPageResponseDto> searchUsersBySchool(
            @Parameter(description = "검색할 학교명", example = "성공회대학교") String school,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = DEFAULT_PAGE) int page,
            @Parameter(description = "페이지 크기", example = DEFAULT_SIZE) int size,
            @Parameter(description = "정렬 기준 필드", example = DEFAULT_SORT_BY) String sortBy,
            @Parameter(description = "정렬 방향 (ASC/DESC)", example = DEFAULT_ORDER) SortOrder order
    );
}
