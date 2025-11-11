package com.skhu.gdgocteambuildingproject.projectgallery.dto;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record GalleryProjectInfoResponseDto(
        Long galleryProjectId,
        String projectName,
        String generation,
        String shortDescription,
        ServiceStatus serviceStatus,
        String description,
        List<GalleryProjectMemberResponseDto> members,
        List<GalleryProjectFileInfoResponseDto> files
) {
}
