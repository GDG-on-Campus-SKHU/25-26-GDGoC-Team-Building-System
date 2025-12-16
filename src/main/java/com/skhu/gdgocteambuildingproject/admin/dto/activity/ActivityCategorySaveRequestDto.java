package com.skhu.gdgocteambuildingproject.admin.dto.activity;

public record ActivityCategorySaveRequestDto(
        String categoryName,
        boolean published
) {
}
