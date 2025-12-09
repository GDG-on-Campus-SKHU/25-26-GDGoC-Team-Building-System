package com.skhu.gdgocteambuildingproject.admin.dto;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import lombok.Builder;

@Builder
public record UserSearchResponseDto(
        Long id,
        String name,
        String school,
        String generation,
        Part part
) {
}

