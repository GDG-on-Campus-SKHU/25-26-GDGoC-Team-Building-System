package com.skhu.gdgocteambuildingproject.mypage.model;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.MypageProjectGalleryResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.MypageProjectMemberResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
public class MypageProjectGalleryMapper {

    private static final Map<Part, Integer> PART_ORDER = Map.of(
            Part.WEB, 1,
            Part.BACKEND, 2,
            Part.DESIGN, 3,
            Part.AI, 4,
            Part.PM, 5
    );

    public MypageProjectGalleryResponseDto toDto(GalleryProjectMember myMember) {
        GalleryProject project = myMember.getProject();

        List<GalleryProjectMember> members = project.getMembers();

        MypageProjectMemberResponseDto leader = members.stream()
                .filter(m -> m.getRole() == MemberRole.LEADER)
                .map(MypageProjectMemberResponseDto::from)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("프로젝트에 LEADER가 없습니다. projectId=" + project.getId())
                );

        List<MypageProjectMemberResponseDto> memberDtos = members.stream()
                .filter(m -> m.getRole() == MemberRole.MEMBER) // 리더 제외
                .sorted(
                        Comparator
                                .comparingInt((GalleryProjectMember m) ->
                                        PART_ORDER.getOrDefault(m.getPart(), Integer.MAX_VALUE))
                                .thenComparing(m -> m.getUser().getId())
                )
                .map(MypageProjectMemberResponseDto::from)
                .toList();

        return MypageProjectGalleryResponseDto.builder()
                .projectId(project.getId())
                .thumbnailImageUrl(project.getThumbnailUrl())
                .projectName(project.getProjectName())
                .exhibited(project.getExhibited())
                .shortIntroduction(project.getShortDescription())
                .myRole(myMember.getRole())
                .leader(leader)
                .members(memberDtos)
                .build();
    }
}
