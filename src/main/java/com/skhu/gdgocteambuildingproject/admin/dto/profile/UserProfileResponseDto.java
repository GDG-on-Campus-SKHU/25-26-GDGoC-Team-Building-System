package com.skhu.gdgocteambuildingproject.admin.dto.profile;

import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserGenerationResponseDto;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.TechStackResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.UserLinkResponseDto;
import lombok.Builder;

import java.util.List;

@Builder
public record UserProfileResponseDto(
        Long userId,
        String name,
        String school,
        List<ApprovedUserGenerationResponseDto> generations,
        Part part,
        List<TechStackResponseDto> techStacks,
        List<UserLinkResponseDto> userLinks,
        String introduction
) {
}
