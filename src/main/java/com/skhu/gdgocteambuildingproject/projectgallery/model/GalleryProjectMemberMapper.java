package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.GalleryProjectMemberResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GalleryProjectMemberMapper {

    public List<GalleryProjectMemberResponseDto> map(List<GalleryProjectMember> members) {
        return members.stream()
                .map(this::fromEntity)
                .toList();
    }

    private GalleryProjectMemberResponseDto fromEntity(GalleryProjectMember member) {
        return GalleryProjectMemberResponseDto.builder()
                .memberRole(member.getRole().name())
                .name(member.getUser().getName())
                .part(member.getPart().getKoreanName())
                .build();
    }
}
