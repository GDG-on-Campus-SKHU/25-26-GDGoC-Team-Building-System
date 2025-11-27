package com.skhu.gdgocteambuildingproject.mypage.dto.request;

import com.skhu.gdgocteambuildingproject.user.domain.UserLink;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.LinkType;
import lombok.Builder;

@Builder
public record UserLinkDto(
        LinkType linkType,
        String url
) {
    public static UserLinkDto toEntity(UserLink entity) {
        return UserLinkDto.builder()
                .linkType(entity.getLinkType())
                .url(entity.getUrl())
                .build();
    }
}
