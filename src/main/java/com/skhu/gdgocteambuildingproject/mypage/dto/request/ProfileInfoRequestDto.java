package com.skhu.gdgocteambuildingproject.mypage.dto.request;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.user.domain.TechStack;
import com.skhu.gdgocteambuildingproject.user.domain.UserLink;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfileInfoRequestDto(
        String school,
        UserRole role,
        Part part,
        List<TechStackDto> techStacks,
        List<UserLinkDto> userLinks,
        String introduction
) {
}
