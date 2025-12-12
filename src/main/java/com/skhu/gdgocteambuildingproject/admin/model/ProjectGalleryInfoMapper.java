package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import org.springframework.stereotype.Component;

@Component
public class ProjectGalleryInfoMapper {
    public ProjectGalleryResponseDto toDto(GalleryProject galleryProject) {
        return ProjectGalleryResponseDto.builder()
                .id(galleryProject.getId())
                .projectName(galleryProject.getProjectName())
                .serviceStatus(galleryProject.getServiceStatus())
                .generation(galleryProject.getGeneration().getLabel())
                .createdAt(galleryProject.getCreatedAt())
                .build();
    }
}
