package com.skhu.gdgocteambuildingproject.mypage.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record ProfileInfoUpdateRequestDto(
        List<UserTechStackUpdateRequestDto> techStacks,
        List<UserLinkUpdateRequestDto> userLinks,
        String introduction
) {
}
