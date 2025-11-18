package com.skhu.gdgocteambuildingproject.admin.api;

import com.skhu.gdgocteambuildingproject.admin.dto.ApproveUserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin User Management", description = "관리자 유저 승인 관리 API")
public interface AdminUserProfileApi {

    @Operation(summary = "승인된 유저 목록 조회", description = "관리자가 승인된 유저들의 목록을 페이징하여 조회합니다.")
    ResponseEntity<ApproveUserInfoPageResponseDto> getApproveAllUsers(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") int page,
            @Parameter(description = "페이지 크기", example = "20") int size,
            @Parameter(description = "정렬 기준 필드", example = "id") String sortBy,
            @Parameter(description = "정렬 방향 (ASC/DESC)") SortOrder order
    );
}
