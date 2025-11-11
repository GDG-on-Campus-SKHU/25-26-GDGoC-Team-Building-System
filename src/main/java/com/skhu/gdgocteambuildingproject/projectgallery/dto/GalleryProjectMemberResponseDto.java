package com.skhu.gdgocteambuildingproject.projectgallery.dto;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import lombok.Builder;

@Builder
public record GalleryProjectMemberResponseDto(
        MemberRole memberRole,
        String name,
        Part part
) {
}
