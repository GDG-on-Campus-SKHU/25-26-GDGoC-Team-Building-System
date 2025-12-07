package com.skhu.gdgocteambuildingproject.admin.dto.project;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import lombok.Builder;

@Builder
public record ProjectAvailablePartResponseDto(
        Part part,
        boolean available
) {
}
