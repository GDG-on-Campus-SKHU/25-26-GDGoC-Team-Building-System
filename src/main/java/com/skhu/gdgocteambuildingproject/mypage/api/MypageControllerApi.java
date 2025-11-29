package com.skhu.gdgocteambuildingproject.mypage.api;

import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "마이페이지 API",
        description = "모든 유저가 사용 가능한 마이페이지 API"
)
public interface MypageControllerApi {

    @Operation(
            summary = "프로필 조회",
            description = "마이페이지의 Profile 탭에서 사용자의 본인의 프로필을 조회합니다."
    )
    @ApiResponse(responseCode = "200")
    ResponseEntity<ProfileInfoResponseDto> getProfileByUserid(@Parameter(description = "프로필을 조회하려는 유저의 유저 ID", example = "1", required = true) Long userId);

    @Operation(
            summary = "프로필 수정",
            description = """
                    마이페이지의 Profile 탭에서 사용자 본인의 프로필을 수정합니다
                    기술스택 혹은 링크를 입력하지 않으면 빈 리스트를 반환합니다
                    """
    )
    @ApiResponse(responseCode = "200")
    ResponseEntity<ProfileInfoResponseDto> updateModifiableProfile(@Parameter(description = "프로필을 수정하려는 유저의 유저 ID", example = "1", required = true) Long userId,
                                                                   @RequestBody ProfileInfoUpdateRequestDto profileInfoRequestDto);
}
