package com.skhu.gdgocteambuildingproject.admin.api;

import com.skhu.gdgocteambuildingproject.admin.dto.UserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.UserSearchResponseDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "관리자 멤버관리 API", description = "관리자용 멤버관리 API입니다")
public interface AdminUserManageApi {

    String DEFAULT_PAGE = "0";
    String DEFAULT_SIZE = "20";
    String DEFAULT_SORT_BY = "id";
    String DEFAULT_ORDER = "ASC";

    @Operation(summary = "회원 가입 승인", description = "관리자가 특정 회원의 가입을 승인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "승인 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원", content = @Content),
            @ApiResponse(responseCode = "500", description = "이미 승인된 회원", content = @Content)
    })
    ResponseEntity<Void> approveUser(
            @Parameter(description = "승인할 회원의 ID") Long userId
    );

    @Operation(summary = "회원 가입 거절", description = "관리자가 특정 회원의 가입을 거절합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "거절 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원", content = @Content),
            @ApiResponse(responseCode = "500", description = "이미 거절된 회원", content = @Content)
    })
    ResponseEntity<Void> rejectUser(
            @Parameter(description = "거절할 회원의 ID") Long userId
    );

    @Operation(summary = "거부된 회원 상태 초기화", description = "REJECTED 상태인 회원을 대기중 상태로 되돌립니다.")
    @ApiResponse(responseCode = "200", description = "초기화 성공")
    ResponseEntity<Void> resetRejectedUser(
            @Parameter(description = "사용자 ID", example = "1") Long userId
    );

    @Operation(summary = "전체 회원 목록 조회", description = "관리자가 페이지네이션을 통해 전체 회원 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<UserInfoPageResponseDto> getAllUsers(
            @Parameter(description = "페이지 번호 (0부터 시작)") int page,
            @Parameter(description = "페이지 당 항목 수") int size,
            @Parameter(description = "정렬 기준 필드명") String sortBy,
            @Parameter(description = "정렬 순서 (ASC 또는 DESC)") SortOrder order
    );

    @Operation(
            summary = "회원 검색(기수, 학교)",
            description = """
                    기수와 학교명을 기반으로 회원을 검색합니다.
                    학교명(schools 파라미터)는 여러 개 전달할 수 있습니다.
                    
                    승인되지 않은 회원, 밴 된 회원, 소프트 딜리트된 회원은 조회되지 않습니다.
                    """
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<UserSearchResponseDto>> searchUsers(
            @Parameter(description = "기수 (22-23, 23-24 등)") String generation,
            @Parameter(description = "학교명 (여러 개 가능)") List<String> schools
    );
}
