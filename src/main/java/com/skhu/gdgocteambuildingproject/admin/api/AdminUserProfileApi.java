package com.skhu.gdgocteambuildingproject.admin.api;

import com.skhu.gdgocteambuildingproject.admin.dto.ApproveUserInfoPageResponseDto;
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
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") int page,
            @Parameter(description = "페이지 크기", example = "20") int size,
            @Parameter(description = "정렬 기준 필드", example = "id") String sortBy,
            @Parameter(description = "정렬 방향 (ASC/DESC)") SortOrder order
    );

    @Operation(summary = "유저 정지 (Ban)", description = "관리자가 특정 유저를 정지 처리합니다.")
    ResponseEntity<Void> banUser(
            @Parameter(description = "정지할 유저 ID", example = "1", required = true) Long userId
    );

    @Operation(summary = "유저 정지 해제 (Unban)", description = "관리자가 특정 유저의 정지를 해제합니다.")
    ResponseEntity<Void> unbanUser(
            @Parameter(description = "정지 해제할 유저 ID", example = "1", required = true) Long userId
    );
}
