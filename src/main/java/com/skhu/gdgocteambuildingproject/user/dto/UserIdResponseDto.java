package com.skhu.gdgocteambuildingproject.user.dto;

import lombok.Builder;

@Builder
public record UserIdResponseDto(
        Long userId
) {
}
