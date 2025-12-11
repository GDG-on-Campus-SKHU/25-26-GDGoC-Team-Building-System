package com.skhu.gdgocteambuildingproject.mypage.dto.response;

import com.skhu.gdgocteambuildingproject.user.domain.UserLink;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.LinkType;
import lombok.Builder;

@Builder
public record UserLinkResponseDto(
        LinkType linkType,
        String url,
        String iconUrl
) {
    public static UserLinkResponseDto from(UserLink entity) {
        return UserLinkResponseDto.builder()
                .linkType(entity.getLinkType())
                .url(entity.getUrl())
                .iconUrl(entity.getLinkType().getLinkIconUrl())
                .build();
    }
}
