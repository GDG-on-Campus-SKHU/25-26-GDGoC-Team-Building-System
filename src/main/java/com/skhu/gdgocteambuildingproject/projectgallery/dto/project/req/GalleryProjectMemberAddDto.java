package com.skhu.gdgocteambuildingproject.projectgallery.dto.project.req;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;

public record GalleryProjectMemberAddDto(
        Long userId,
        Part part
) {
}
