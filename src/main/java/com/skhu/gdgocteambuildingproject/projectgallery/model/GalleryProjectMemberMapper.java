package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.MemberSearchListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.MemberSearchResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.GalleryProjectMemberResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GalleryProjectMemberMapper {

    public List<GalleryProjectMemberResponseDto> mapMembersInfo(List<GalleryProjectMember> members) {
        return members.stream()
                .map(this::memberFromEntity)
                .toList();
    }

    public MemberSearchListResponseDto mapSearchMembers(List<User> users, List<Long> selectedUserIds) {
        return mapToListDto(
                users.stream()
                .map(user -> userFromEntity(user, selectedUserIds.contains(user.getId())))
                .toList());
    }

    private GalleryProjectMemberResponseDto memberFromEntity(GalleryProjectMember member) {
        return GalleryProjectMemberResponseDto.builder()
                .memberRole(member.getRole().name())
                .name(member.getUser().getName())
                .part(member.getPart().getKoreanName())
                .build();
    }

    private MemberSearchResponseDto userFromEntity(User user, boolean selected) {
        return MemberSearchResponseDto.builder()
                .name(user.getName())
                .school(user.getSchool())
                .generationAndPosition(user.getGeneration() + " " + user.getPosition())
                .isSelected(selected)
                .build();
    }

    private MemberSearchListResponseDto mapToListDto(List<MemberSearchResponseDto> memberSearchResponseDtos) {
        return MemberSearchListResponseDto.builder()
                .members(memberSearchResponseDtos)
                .build();
    }
}
