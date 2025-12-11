package com.skhu.gdgocteambuildingproject.mypage.dto.response;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.mypage.dto.TechStackDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.UserLinkDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.UserGenerationResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfileInfoResponseDto(
        Long userId,
        String name,
        String school,
        List<UserGenerationResponseDto> generations,
        Part part,
        List<TechStackDto> techStacks,
        List<UserLinkDto> userLinks,
        String introduction
) {
}
