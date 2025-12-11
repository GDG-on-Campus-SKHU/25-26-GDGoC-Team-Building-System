package com.skhu.gdgocteambuildingproject.mypage.api;

import com.skhu.gdgocteambuildingproject.global.jwt.service.UserPrincipal;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "마이페이지 API",
        description = "모든 유저가 사용 가능한 마이페이지 API"
)
public interface MypageControllerApi {

    @Operation(
            summary = "프로필 조회",
            description = """
                    마이페이지의 Profile 탭에서 로그인된 사용자 본인의 프로필을 조회합니다.
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
                    마이페이지의 Profile 탭에서 로그인된 ㅊ사용자 본인의 프로필을 수정합니다
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
}
