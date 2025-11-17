package com.skhu.gdgocteambuildingproject.projectgallery.dto.project;

import lombok.Builder;

@Builder
public record GalleryProjectSummaryResponseDto(
        Long galleryProjectId,
        String projectName,
        String shortDescription,
        String serviceStatus,
        GalleryProjectFileInfoResponseDto fileUrl
) {
}
