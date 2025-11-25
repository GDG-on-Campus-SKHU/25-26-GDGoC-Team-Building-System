package com.skhu.gdgocteambuildingproject.admin.dto.activity;

public record PostSaveDto(
        String title,
        String speaker,
        String generation,
        String videoUrl,
        String thumbnailUrl
) {
}
