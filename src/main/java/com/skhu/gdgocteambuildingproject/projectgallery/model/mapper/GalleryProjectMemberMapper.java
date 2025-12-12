package com.skhu.gdgocteambuildingproject.projectgallery.model.mapper;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.MemberSearchListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.MemberSearchResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectMemberResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GalleryProjectMemberMapper {

    public List<GalleryProjectMemberResponseDto> mapMembersInfo(List<GalleryProjectMember> members) {
        return members.stream()
                .map(this::memberFromEntity)
                .toList();
    }

    public MemberSearchListResponseDto mapSearchMembers(List<User> users) {
        return mapToListDto(
                users.stream()
                .map(this::userFromEntity)
                .toList());
    }

    private GalleryProjectMemberResponseDto memberFromEntity(GalleryProjectMember member) {
        return GalleryProjectMemberResponseDto.builder()
                .memberRole(member.getRole())
                .name(member.getUser().getName())
                .part(member.getPart())
                .build();
    }

    private MemberSearchResponseDto userFromEntity(User user) {
        return MemberSearchResponseDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .school(user.getSchool())
                .generationAndPosition(joinGenerationAndPositions(user))
                .isSelected(false)
                .build();
    }

    private String joinGenerationAndPositions(User user) {
        return user.getGeneration().stream()
                .map(gen -> gen.getGeneration().getLabel() + " " + gen.getPosition().name())
                .collect(Collectors.joining(", "));
    }

    private MemberSearchListResponseDto mapToListDto(List<MemberSearchResponseDto> memberSearchResponseDtos) {
        return MemberSearchListResponseDto.builder()
                .members(memberSearchResponseDtos)
                .build();
    }
}
