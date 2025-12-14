package com.skhu.gdgocteambuildingproject.mypage.api;

import com.skhu.gdgocteambuildingproject.global.jwt.service.UserPrincipal;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.MypageProjectGalleryResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.TechStackOptionsResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.UserLinkOptionsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(
        name = "마이페이지 API",
        description = "모든 유저가 사용 가능한 마이페이지 API"
)
public interface MypageControllerApi {

    @Operation(
            summary = "프로필 조회",
            description = """
                    마이페이지의 Profile 탭에서 현재 로그인한 사용자 본인의 프로필을 조회합니다.
                    
                    현재 로그인한 사용자의 프로필만 조회가 가능합니다.
                    """
    )
    @ApiResponse(responseCode = "200", description = "프로필 조회 성공")
    ResponseEntity<ProfileInfoResponseDto> getProfileByUserPrincipal(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @Operation(
            summary = "프로필 수정",
            description = """
                    마이페이지의 Profile 탭에서 현재 로그인한 사용자 본인의 프로필을 수정합니다.
                    
                    기술스택 혹은 링크를 입력하지 않으면 빈 리스트를 반환합니다
                    """
    )
    @ApiResponse(responseCode = "204")
    ResponseEntity<ProfileInfoResponseDto> updateUserModifiableProfile(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody ProfileInfoUpdateRequestDto profileInfoRequestDto
    );

    @Operation(
            summary = "아이디어 멤버 프로필 조회",
            description = """
                    아이디어 멤버 ID를 통해 해당 유저의 프로필을 조회합니다.
                    
                    아이디어 멤버와 연결된 유저 객체를 찾아 해당 유저의 프로필 정보를 반환합니다.
                    """
    )
    @ApiResponse(responseCode = "200")
    ResponseEntity<ProfileInfoResponseDto> getProfileByIdeaMemberId(
            @Parameter(description = "아이디어 멤버 ID", example = "1") Long ideaMemberId
    );

    @Operation(
            summary = "모든 기술스택 옵션 조회",
            description = "프로필 수정 시 사용자가 선택할 수 있는 모든 기술스택 옵션을 조회합니다."
    )
    @ApiResponse(responseCode = "200")
    ResponseEntity<List<TechStackOptionsResponseDto>> getAllTechStacks();

    @Operation(
            summary = "모든 유저링크 옵션 조회",
            description = "프로필 수정 시 사용자가 선택할 수 있는 모든 유저링크 옵션을 조회합니다."
    )
    @ApiResponse(responseCode = "200")
    ResponseEntity<List<UserLinkOptionsResponseDto>> getLinkTypeOptions();

    @Operation(
            summary = "내 프로젝트 목록 조회",
            description = """
                    마이페이지의 my project 탭에서 현재 로그인한 사용자 본인의 프로젝트 목록을 조회합니다.
                    
                    팀원일 경우 프로젝트의 정보만 반환, 팀장일 경우 프로젝트의 정보와 전시 여부 정보를 반환합니다.
                    
                    현재 로그인한 사용자의 프로젝트 목록만 조회가 가능합니다.
                    
                    가장 최근에 등록한 순서로 정렬됩니다.
                    
                    (중요) 참여한 프로젝트가 없다면 빈 리스트를 반환합니다.
                    """
    )
    @ApiResponse(responseCode = "200", description = "프로젝트 목록 조회 성공")
    ResponseEntity<List<MypageProjectGalleryResponseDto>> getUserGalleryProjects(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal userPrincipal
    );
}
