package com.skhu.gdgocteambuildingproject.mypage.api;

import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "프로필 관리 API",
        description = "모든 유저가 사용 가능한 마이페이지의 프로필 조회 및 수정 API"
)
public interface MyPageControllerApi {

    @Operation(
            summary = "프로필 조회",
            description = "마이페이지의 Profile 탭에서 사용자의 프로필을 상세 조회합니다."
    )
    @ApiResponse(responseCode = "200")
    ResponseEntity<ProfileInfoResponseDto> getProfileByUserid(@PathVariable Long userId);

    @Operation(
            summary = "프로필 수정",
            description = "마이페이지의 Profile 탭에서 사용자의 프로필을 수정합니다"
    )
    @ApiResponse(responseCode = "200")
    ResponseEntity<ProfileInfoResponseDto> updateProfile(@Parameter(description = "조회할 프로필의 유저 ID", required = true) Long userId,
                                                         @RequestBody ProfileInfoRequestDto profileInfoRequestDto);
}
