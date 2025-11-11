package com.skhu.gdgocteambuildingproject.admin.dto;

import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import lombok.Builder;

@Builder
public record UserResponseDto(
        Long id,
        String userName,
        String email,
        String generation,
        String number,
        String part,
        String school,
        ApprovalStatus approvalStatus
) {
}
