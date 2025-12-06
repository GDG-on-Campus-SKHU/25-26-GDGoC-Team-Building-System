package com.skhu.gdgocteambuildingproject.admin.dto.activity;

public record ActivityUpdateRequestDto(
        String categoryName,
        boolean published
) {
}
