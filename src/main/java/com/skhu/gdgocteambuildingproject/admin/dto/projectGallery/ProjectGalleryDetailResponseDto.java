package com.skhu.gdgocteambuildingproject.admin.dto.projectGallery;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record ProjectGalleryDetailResponseDto(
        Long projectId,
        String projectName,
        String generation,
        String shortDescription,
        ServiceStatus serviceStatus,
        boolean exhibited,
        String description,
        ProjectMemberResponseDto leader,
        List<ProjectMemberResponseDto> members
) {
}
