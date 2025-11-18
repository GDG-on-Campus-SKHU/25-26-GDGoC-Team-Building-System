package com.skhu.gdgocteambuildingproject.admin.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ApproveUserInfoPageResponseDto(
        List<ApprovedUserResponseDto> users,
        PageInfo pageInfo
) {
}
