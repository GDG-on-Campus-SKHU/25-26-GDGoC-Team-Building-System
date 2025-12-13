package com.skhu.gdgocteambuildingproject.admin.dto.projectGallery;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProjectGalleryResponseDto(
        Long id,
        String projectName,
        String generation,
        boolean exhibited,
        LocalDateTime createdAt
) {
}
