package com.skhu.gdgocteambuildingproject.admin.dto;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import lombok.Builder;

import java.util.List;

@Builder
public record ApprovedUserResponseDto(
        Long id,
        String userName,
        String school,
        Part part,
        List<String> generation,
        List<String> userPosition
) {
}
