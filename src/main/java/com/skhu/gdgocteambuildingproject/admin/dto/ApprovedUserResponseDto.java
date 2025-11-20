package com.skhu.gdgocteambuildingproject.admin.dto;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import lombok.Builder;

@Builder
public record ApprovedUserResponseDto(
        Long id,
        String userName,
        String school,
        Part part,
        String generation,
        UserPosition userPosition
) {
}
