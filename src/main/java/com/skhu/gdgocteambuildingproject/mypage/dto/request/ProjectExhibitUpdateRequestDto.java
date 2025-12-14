package com.skhu.gdgocteambuildingproject.mypage.dto.request;

public record ProjectExhibitUpdateRequestDto(
        Long projectId,
        boolean exhibited
) {
}
