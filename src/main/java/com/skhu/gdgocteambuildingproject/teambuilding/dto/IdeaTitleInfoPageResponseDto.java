package com.skhu.gdgocteambuildingproject.teambuilding.dto;

import com.skhu.gdgocteambuildingproject.global.pagination.PageInfo;
import java.util.List;
import lombok.Builder;

@Builder
public record IdeaTitleInfoPageResponseDto(
        List<IdeaTitleInfoResponseDto> ideas,
        PageInfo pageInfo
) {
}
