package com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res;

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
        GalleryProjectMemberResponseDto leader,
        List<GalleryProjectMemberResponseDto> members,
        String thumbnailUrl
) {
}
