package com.skhu.gdgocteambuildingproject.mypage.dto.response;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.mypage.dto.TechStackDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.UserLinkDto;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfileInfoResponseDto(
        Long userId,
        Part part,
        List<TechStackDto> techStacks,
        List<UserLinkDto> userLinks,
        String introduction
) {
}
