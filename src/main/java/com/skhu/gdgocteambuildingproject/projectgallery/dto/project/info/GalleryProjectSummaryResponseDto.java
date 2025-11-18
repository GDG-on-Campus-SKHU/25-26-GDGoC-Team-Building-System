package com.skhu.gdgocteambuildingproject.projectgallery.dto.project.info;

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
