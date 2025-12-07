package com.skhu.gdgocteambuildingproject.admin.dto;

public record ApprovedUserGenerationResponseDto(
        Long id,
        String generation,
        String position,
        boolean isMain
) {
}
