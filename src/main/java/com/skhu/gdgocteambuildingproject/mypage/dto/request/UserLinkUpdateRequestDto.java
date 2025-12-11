package com.skhu.gdgocteambuildingproject.mypage.dto.request;

import com.skhu.gdgocteambuildingproject.user.domain.UserLink;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.LinkType;
import lombok.Builder;

@Builder
public record UserLinkUpdateRequestDto(
        LinkType linkType,
        String url
) {
    public static UserLinkUpdateRequestDto from(UserLink entity) {
        return UserLinkUpdateRequestDto.builder()
                .linkType(entity.getLinkType())
                .url(entity.getUrl())
                .build();
    }
}