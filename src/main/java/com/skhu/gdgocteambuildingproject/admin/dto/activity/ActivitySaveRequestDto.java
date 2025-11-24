package com.skhu.gdgocteambuildingproject.admin.dto.activity;

import java.util.List;

public record ActivitySaveRequestDto(
        String categoryName,
        boolean published,
        List<PostSaveDto> posts
) {
}
