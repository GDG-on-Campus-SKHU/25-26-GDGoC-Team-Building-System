package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.info.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.info.GalleryProjectListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.info.GalleryProjectSummaryResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProjectInfoMapper {

    private final GalleryProjectMemberMapper memberMapper;
    private final GalleryProjectFileMapper fileMapper;

    public GalleryProjectInfoResponseDto mapToInfo(GalleryProject project) {
        return GalleryProjectInfoResponseDto.builder()
                .galleryProjectId(project.getId())
                .projectName(project.getProjectName())
                .generation(project.getGeneration())
                .shortDescription(project.getShortDescription())
                .serviceStatus(project.getServiceStatus().name())
                .description(project.getDescription())
                .leaderId(project.getUser().getId())
                .members(memberMapper.mapMembersInfo(project.getMembers()))
                .files(fileMapper.map(project.getFiles()))
                .build();
    }

    public GalleryProjectSummaryResponseDto mapToSummary(GalleryProject project) {
        return GalleryProjectSummaryResponseDto.builder()
                .galleryProjectId(project.getId())
                .projectName(project.getProjectName())
                .shortDescription(project.getShortDescription())
                .serviceStatus(project.getServiceStatus().name())
                .fileUrl(
                        fileMapper.map(project.getFiles()).stream()
                            .findFirst()
                            .orElse(null)
                )
                .build();
    }

    public GalleryProjectListResponseDto mapToListDto(List<GalleryProjectSummaryResponseDto> galleryProjectInfoResponseDtoList) {
        return GalleryProjectListResponseDto.builder()
                .galleryProjectSummaryResponseDtoList(galleryProjectInfoResponseDtoList)
                .build();
    }
}
