package com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res;

import lombok.Builder;

@Builder
public record GalleryProjectSummaryResponseDto(
        Long galleryProjectId,
        String generation,
        String projectName,
        String shortDescription,
        String serviceStatus,
        String thumbnailUrl
) {
}
