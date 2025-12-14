package com.skhu.gdgocteambuildingproject.mypage.dto.response;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import lombok.Builder;

@Builder
public record MypageProjectGalleryResponseDto(
        String thumbnailImageUrl,
        String projectName,
        boolean exhibited,
        String shortIntroduction,
        MemberRole myRole
) {
    public static MypageProjectGalleryResponseDto from(GalleryProject project, MemberRole myRole) {
        return MypageProjectGalleryResponseDto.builder()
                .thumbnailImageUrl(project.getThumbnailUrl())
                .projectName(project.getProjectName())
                .exhibited(project.isExhibited())
                .shortIntroduction(project.getShortDescription())
                .myRole(myRole)
                .build();
    }
}
