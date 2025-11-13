package com.skhu.gdgocteambuildingproject.admin.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UserInfoPageResponseDto(
        List<UserResponseDto> users,
        PageInfo pageInfo
) {
}
