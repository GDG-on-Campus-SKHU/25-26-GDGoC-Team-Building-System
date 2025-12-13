package com.skhu.gdgocteambuildingproject.projectgallery.model.mapper;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectSummaryResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProjectInfoMapper {

    private final GalleryProjectMemberMapper memberMapper;

    public GalleryProjectInfoResponseDto mapToInfo(GalleryProject project) {
        return GalleryProjectInfoResponseDto.builder()
                .galleryProjectId(project.getId())
                .projectName(project.getProjectName())
                .generation(project.getGeneration().getLabel())
                .shortDescription(project.getShortDescription())
                .serviceStatus(project.getServiceStatus().name())
                .description(project.getDescription())
                .leader(memberMapper.mapLeaderInfo(project.getMembers()))
                .members(memberMapper.mapMembersInfo(project.getMembers()))
                .thumbnailUrl(project.getThumbnailUrl())
                .build();
    }

    public GalleryProjectSummaryResponseDto mapToSummary(GalleryProject project) {
        return GalleryProjectSummaryResponseDto.builder()
                .galleryProjectId(project.getId())
                .generation(project.getGeneration().getLabel())
                .projectName(project.getProjectName())
                .shortDescription(project.getShortDescription())
                .serviceStatus(project.getServiceStatus().name())
                .thumbnailUrl(project.getThumbnailUrl())
                .build();
    }

    public GalleryProjectListResponseDto mapToListDto(List<GalleryProjectSummaryResponseDto> galleryProjectInfoResponseDtoList) {
        return GalleryProjectListResponseDto.builder()
                .galleryProjectSummaryResponseDtoList(galleryProjectInfoResponseDtoList)
                .build();
    }
}
