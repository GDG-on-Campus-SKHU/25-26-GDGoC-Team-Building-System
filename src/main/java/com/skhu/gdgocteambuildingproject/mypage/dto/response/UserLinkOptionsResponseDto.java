package com.skhu.gdgocteambuildingproject.mypage.dto.response;

import com.skhu.gdgocteambuildingproject.user.domain.enumtype.LinkType;

public record UserLinkOptionsResponseDto(
        String type,
        String name,
        String iconUrl
) {
    public static UserLinkOptionsResponseDto from(LinkType linkType) {
        return new UserLinkOptionsResponseDto(
                linkType.name(),
                linkType.getDisplayName(),
                linkType.getLinkIconUrl()
        );
    }
}
