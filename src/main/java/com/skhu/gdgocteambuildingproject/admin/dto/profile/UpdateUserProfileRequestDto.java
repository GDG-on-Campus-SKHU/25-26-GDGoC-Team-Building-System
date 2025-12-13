package com.skhu.gdgocteambuildingproject.admin.dto.profile;

import com.skhu.gdgocteambuildingproject.mypage.dto.request.UserLinkUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.UserTechStackUpdateRequestDto;

import java.util.List;

public record UpdateUserProfileRequestDto(
        List<UserTechStackUpdateRequestDto> techStacks,
        List<UserLinkUpdateRequestDto> userLinks,
        String introduction
) {
}

