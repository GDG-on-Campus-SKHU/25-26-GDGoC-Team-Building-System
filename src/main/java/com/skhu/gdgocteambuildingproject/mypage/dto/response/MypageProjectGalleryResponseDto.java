package com.skhu.gdgocteambuildingproject.mypage.dto.response;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import lombok.Builder;

import java.util.List;

@Builder
public record MypageProjectGalleryResponseDto(
        Long projectId,
        String thumbnailImageUrl,
        String projectName,
        boolean exhibited,
        String shortIntroduction,
        MemberRole myRole,
        MypageProjectMemberResponseDto leader,
        List<MypageProjectMemberResponseDto> members
) {
}