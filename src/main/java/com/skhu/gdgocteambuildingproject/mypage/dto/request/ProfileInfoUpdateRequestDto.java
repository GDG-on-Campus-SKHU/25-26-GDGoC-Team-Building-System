package com.skhu.gdgocteambuildingproject.mypage.dto.request;

import com.skhu.gdgocteambuildingproject.mypage.dto.TechStackDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.UserLinkDto;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfileInfoUpdateRequestDto(
        List<TechStackDto> techStacks,
        List<UserLinkDto> userLinks,
        String introduction
) {
}
