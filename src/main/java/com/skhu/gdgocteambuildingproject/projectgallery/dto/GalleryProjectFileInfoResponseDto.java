package com.skhu.gdgocteambuildingproject.projectgallery.dto;

import lombok.Builder;

@Builder
public record GalleryProjectFileInfoResponseDto(
        String fileUrl
) {
}
