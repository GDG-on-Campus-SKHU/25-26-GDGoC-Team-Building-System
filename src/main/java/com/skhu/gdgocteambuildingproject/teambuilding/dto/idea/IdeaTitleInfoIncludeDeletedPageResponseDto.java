package com.skhu.gdgocteambuildingproject.teambuilding.dto.idea;

import com.skhu.gdgocteambuildingproject.global.pagination.PageInfo;
import java.util.List;
import lombok.Builder;

@Builder
public record IdeaTitleInfoIncludeDeletedPageResponseDto(
        List<IdeaTitleInfoIncludeDeletedResponseDto> ideas,
        PageInfo pageInfo
) {
}
