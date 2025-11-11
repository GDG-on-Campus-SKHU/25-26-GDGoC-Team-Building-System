package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.GalleryProjectInfoResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProjectInfoMapper {

    private final GalleryProjectMemberMapper memberMapper;
    private final GalleryProjectFileMapper fileMapper;

    public GalleryProjectInfoResponseDto map(GalleryProject project) {
        return GalleryProjectInfoResponseDto.builder()
                .galleryProjectId(project.getId())
                .projectName(project.getProjectName())
                .generation(project.getGeneration())
                .shortDescription(project.getShortDescription())
                .serviceStatus(project.getServiceStatus().name())
                .description(project.getDescription())
                .members(memberMapper.map(project.getMembers()))
                .files(fileMapper.map(project.getFiles()))
                .build();
    }
}
