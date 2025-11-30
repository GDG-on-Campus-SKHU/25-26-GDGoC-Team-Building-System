package com.skhu.gdgocteambuildingproject.mypage.dto;

import com.skhu.gdgocteambuildingproject.user.domain.UserLink;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.LinkType;
import lombok.Builder;

@Builder
public record UserLinkDto(
        LinkType linkType,
        String url,
        String iconUrl
) {
    public static UserLinkDto from(UserLink entity) {
        return UserLinkDto.builder()
                .linkType(entity.getLinkType())
                .url(entity.getUrl())
                .iconUrl(entity.getLinkType().getLinkIconUrl())
                .build();
    }
}
