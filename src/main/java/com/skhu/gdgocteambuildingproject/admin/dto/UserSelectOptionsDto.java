package com.skhu.gdgocteambuildingproject.admin.dto;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import lombok.Builder;

import java.util.List;

@Builder
public record UserSelectOptionsDto(
        List<Part> parts,
        List<UserPosition> positions,
        List<String> generations
) {
}
