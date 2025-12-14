package com.skhu.gdgocteambuildingproject.admin.dto.projectGallery;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;

import java.util.List;

public record ProjectGalleryUpdateRequestDto(
        String projectName,
        String generation,
        String shortDescription,
        ServiceStatus serviceStatus,
        boolean exhibited,
        String description,
        Long leaderId,
        String leaderPart,
        List<GalleryProjectMemberUpdateDto> members,
        String thumbnailUrl
) {
}
