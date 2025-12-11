package com.skhu.gdgocteambuildingproject.admin.api;

import com.skhu.gdgocteambuildingproject.admin.dto.ApproveUserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.ApproveUserUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserInfoResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.UserBanRequestDto;
import com.skhu.gdgocteambuildingproject.admin.dto.profile.UpdateUserProfileRequestDto;
import com.skhu.gdgocteambuildingproject.admin.dto.profile.UserProfileResponseDto;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "유저 기수-역할(UserGeneration) 삭제",
            description = """
            관리자가 특정 유저의 기수-역할(UserGeneration) 정보를 삭제합니다.
            요청한 ID가 존재하지 않을 경우 404 Not Found를 반환합니다.
            삭제 성공 시 204 OK를 반환하며 본문은 없습니다.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 Generation", content = @Content),
    })
    ResponseEntity<Void> deleteUserGeneration(
            @Parameter(description = "삭제할 UserGeneration ID", example = "1", required = true)
            Long generationId
    );

    @Operation(
            summary = "승인된 회원 상세 조회",
            description = "관리자가 특정 회원의 승인 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원", content = @Content),
    })
    ResponseEntity<ApprovedUserInfoResponseDto> getApproveUser(
            @Parameter(description = "조회할 회원 ID", example = "1", required = true)
            Long userId
    );

    @Operation(
            summary = "승인된 회원 정보 수정",
            description = """
            관리자가 특정 회원의 학교, 파트, 기수, 역할 정보를 수정합니다.

            - **기존 기수 및 역할 항목 수정 시:** 해당 항목의 `id` 값을 함께 전달해야 합니다.
            - **새로운 기수/역할 추가 시:** `id` 없이 generation(ex : 24-25)과 position(ex : CORE) 정보를 전달하면 됩니다.
            - **대표 기수 설정:** `isMain` 필드를 통해 어떤 기수/역할이 멤버 리스트에서 대표로 보여질 지 지정합니다. (true/false)

            요청이 성공하면 200 OK를 반환하며, 응답 본문은 없습니다.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원", content = @Content),
    })
    ResponseEntity<Void> updateApproveUser(
            @Parameter(description = "수정할 회원 ID", example = "1", required = true)
            Long userId,

            @RequestBody(
                    description = "회원 정보 수정 DTO",
                    required = true
            )
            ApproveUserUpdateRequestDto dto
    );

    @Operation(summary = "회원의 마이페이지 프로필 단건 조회", description = "관리자가 특정 회원의 마이페이지 프로필 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원", content = @Content)
    })
    ResponseEntity<UserProfileResponseDto> getProfileByUser(
            @Parameter(description = "조회할 회원의 ID", example = "1", required = true)
            Long userId
    );

    @Operation(summary = "회원의 마이페이지 프로필 수정", description = "관리자가 특정 회원의 프로필 정보를 부분 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원", content = @Content)
    })
    ResponseEntity<UserProfileResponseDto> updateProfileByUser(
            @Parameter(description = "수정할 회원의 ID", example = "1", required = true)
            Long userId,

            @RequestBody(
                    description = """
                        ## 수정할 프로필 정보
                        - **techStacks**: 기술 스택 목록
                        - **userLinks**: 사용자 링크 목록
                        - **introduction**: 자기소개
                        """,
                    required = true
            )
            UpdateUserProfileRequestDto dto
    );
}
