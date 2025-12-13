package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectMemberResponseDto;
import com.skhu.gdgocteambuildingproject.admin.util.GenerationMapper;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectMemberMapper {

    private final GenerationMapper generationMapper;

    public ProjectMemberResponseDto mapLeader(User leader) {
        return ProjectMemberResponseDto.builder()
                .userId(leader.getId())
                .name(leader.getName())
                .school(leader.getSchool())
                .memberRole(MemberRole.LEADER)
                .generations(generationMapper.toMainGenerationDtos(leader))
                .part(leader.getPart())
                .build();
    }

    public List<ProjectMemberResponseDto> mapMembers(List<GalleryProjectMember> members) {
        return members.stream()
                .filter(member -> member.getRole() == MemberRole.MEMBER)
                .map(this::memberFromEntity)
                .toList();
    }

    private ProjectMemberResponseDto memberFromEntity(GalleryProjectMember member) {
        return ProjectMemberResponseDto.builder()
                .userId(member.getUser().getId())
                .name(member.getUser().getName())
                .school(member.getUser().getSchool())
                .memberRole(member.getRole())
                .part(member.getPart())
                .generations(generationMapper.toMainGenerationDtos(member.getUser()))
                .build();
    }
}
