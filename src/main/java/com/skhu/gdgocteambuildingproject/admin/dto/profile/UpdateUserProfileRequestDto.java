package com.skhu.gdgocteambuildingproject.admin.dto.profile;

import com.skhu.gdgocteambuildingproject.mypage.dto.response.TechStackResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.UserLinkResponseDto;

import java.util.List;

public record UpdateUserProfileRequestDto(
        List<TechStackResponseDto> techStacks,
        List<UserLinkResponseDto> userLinks,
        String introduction
) {
}
