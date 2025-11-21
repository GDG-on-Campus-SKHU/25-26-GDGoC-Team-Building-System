package com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res;

import lombok.Builder;

@Builder
public record GalleryProjectFileInfoResponseDto(
        String fileUrl
) {
}
