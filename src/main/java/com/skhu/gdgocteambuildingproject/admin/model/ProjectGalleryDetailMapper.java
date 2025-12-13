package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryDetailResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectGalleryDetailMapper {

    private final ProjectMemberMapper memberMapper;

    public ProjectGalleryDetailResponseDto toDto(GalleryProject galleryProject) {
        return ProjectGalleryDetailResponseDto.builder()
                .projectId(galleryProject.getId())
                .projectName(galleryProject.getProjectName())
                .description(galleryProject.getDescription())
                .shortDescription(galleryProject.getShortDescription())
                .serviceStatus(galleryProject.getServiceStatus())
                .generation(galleryProject.getGeneration().getLabel())
                .exhibited(galleryProject.isExhibited())
                .leader(memberMapper.mapLeader(galleryProject.getUser()))
                .members(memberMapper.mapMembers(galleryProject.getMembers()))
                .build();
    }
}
