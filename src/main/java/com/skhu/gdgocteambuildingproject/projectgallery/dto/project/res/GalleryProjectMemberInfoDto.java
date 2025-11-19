package com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;

public record GalleryProjectMemberInfoDto(
        Long userId,
        MemberRole role,
        Part part
) {
}
