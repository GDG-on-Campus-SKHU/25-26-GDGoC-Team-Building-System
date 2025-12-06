package com.skhu.gdgocteambuildingproject.admin.dto;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;

import java.util.List;

public record ApproveUserUpdateRequestDto(
        List<UserGenerationUpdateDto> generations,
        String school,
        Part part
) {
}
