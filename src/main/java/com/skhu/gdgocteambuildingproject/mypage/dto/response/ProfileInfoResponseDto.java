package com.skhu.gdgocteambuildingproject.mypage.dto.response;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.UserGenerationResponseDto;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfileInfoResponseDto(
        Long userId,
        String name,
        String school,
        List<UserGenerationResponseDto> generations,
        Part part,
        List<UserTechStackResponseDto> techStacks,
        List<UserLinkResponseDto> userLinks,
        String introduction
) {
}
