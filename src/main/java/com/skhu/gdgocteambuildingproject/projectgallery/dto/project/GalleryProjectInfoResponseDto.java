package com.skhu.gdgocteambuildingproject.projectgallery.dto.project;

import lombok.Builder;

import java.util.List;

@Builder
public record GalleryProjectInfoResponseDto(
        Long galleryProjectId,
        String projectName,
        String generation,
        String shortDescription,
        String serviceStatus,
        String description,
        List<GalleryProjectMemberResponseDto> members,
        List<GalleryProjectFileInfoResponseDto> files
) {
}
