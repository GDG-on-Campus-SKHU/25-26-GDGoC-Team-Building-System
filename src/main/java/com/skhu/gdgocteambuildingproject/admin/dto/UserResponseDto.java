package com.skhu.gdgocteambuildingproject.admin.dto;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record UserResponseDto(
        Long id,
        String userName,
        String email,
        List<ApprovedUserGenerationResponseDto> generations,
        String number,
        Part part,
        String school,
        ApprovalStatus approvalStatus
) {
}
