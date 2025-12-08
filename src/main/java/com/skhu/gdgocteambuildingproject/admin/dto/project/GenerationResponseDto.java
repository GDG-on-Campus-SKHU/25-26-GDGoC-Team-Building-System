package com.skhu.gdgocteambuildingproject.admin.dto.project;

import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;

public record GenerationResponseDto(
        String label
) {
    public static GenerationResponseDto from(Generation generation) {
        return new GenerationResponseDto(generation.getLabel());
    }
}
