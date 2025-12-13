package com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import lombok.Builder;

@Builder
public record GalleryProjectMemberResponseDto(
        Long userId,
        MemberRole memberRole,
        String name,
        String school,
        String generationAndPosition,
        Part part
) {
}
