package com.skhu.gdgocteambuildingproject.projectgallery.dto.project.create;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import lombok.Builder;

@Builder
public record GalleryProjectMemberResponseDto(
        Long userId,
        MemberRole memberRole,
        String name,
        Part part
) {
}
