package com.skhu.gdgocteambuildingproject.admin.dto.profile;

import com.skhu.gdgocteambuildingproject.mypage.dto.response.UserTechStackResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.UserLinkResponseDto;

import java.util.List;

public record UpdateUserProfileRequestDto(
        List<UserTechStackResponseDto> techStacks,
        List<UserLinkResponseDto> userLinks,
        String introduction
) {
}
