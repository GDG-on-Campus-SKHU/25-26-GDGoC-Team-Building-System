package com.skhu.gdgocteambuildingproject.admin.dto;

import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;

public record UserGenerationUpdateDto(
        Long id,
        String generation,
        UserPosition position,
        boolean isMain
) {
}
