package com.skhu.gdgocteambuildingproject.projectgallery.dto.project.create;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;

import java.util.List;

public record GalleryProjectCreateRequestDto(
        String projectName,
        String generation,
        String shortDescription,
        ServiceStatus serviceStatus,
        String description,
        Long leaderId,
        List<GalleryProjectMemberInfoDto> members,
        List<Long> fileIds
) {
}
