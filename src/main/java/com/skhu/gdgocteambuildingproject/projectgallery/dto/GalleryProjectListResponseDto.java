package com.skhu.gdgocteambuildingproject.projectgallery.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GalleryProjectListResponseDto(
        List<GalleryProjectSummaryResponseDto> galleryProjectSummaryResponseDtoList
) {
}
