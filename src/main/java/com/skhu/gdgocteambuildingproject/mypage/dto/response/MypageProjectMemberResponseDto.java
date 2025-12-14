package com.skhu.gdgocteambuildingproject.mypage.dto.response;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;

public record MypageProjectMemberResponseDto(
        Long userId,
        String name,
        Part part
) {

    public static MypageProjectMemberResponseDto from(GalleryProjectMember member) {
        return new MypageProjectMemberResponseDto(
                member.getUser().getId(),
                member.getUser().getName(),
                member.getPart()
        );
    }
}
