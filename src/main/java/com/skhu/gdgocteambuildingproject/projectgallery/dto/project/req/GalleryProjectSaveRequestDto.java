package com.skhu.gdgocteambuildingproject.projectgallery.dto.project.req;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;

import java.util.List;

public record GalleryProjectSaveRequestDto(
        String projectName,
        String generation,
        String shortDescription,
        ServiceStatus serviceStatus,
        String description,
        GalleryProjectMemberAddDto leader,
        List<GalleryProjectMemberAddDto> members,
        String thumbnailUrl
) {
}
