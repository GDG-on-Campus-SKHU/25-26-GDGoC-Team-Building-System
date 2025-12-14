package com.skhu.gdgocteambuildingproject.admin.dto.projectGallery;

import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserGenerationResponseDto;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import lombok.Builder;

import java.util.List;

@Builder
public record ProjectMemberResponseDto(
        Long userId,
        String name,
        String school,
        List<ApprovedUserGenerationResponseDto> generations,
        MemberRole memberRole,
        Part part
) {
}
