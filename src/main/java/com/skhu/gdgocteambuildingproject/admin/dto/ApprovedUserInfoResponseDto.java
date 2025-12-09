package com.skhu.gdgocteambuildingproject.admin.dto;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ApprovedUserInfoResponseDto(
        String name,
        String email,
        String phoneNum,
        LocalDate approveAt,
        LocalDate bannedAt,
        LocalDate deletedAt,
        LocalDate unbannedAt,
        UserStatus status,
        List<ApprovedUserGenerationResponseDto> generations,
        String school,
        Part part,
        String banReason
) {
}
