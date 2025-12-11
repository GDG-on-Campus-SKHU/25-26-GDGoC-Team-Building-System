package com.skhu.gdgocteambuildingproject.teambuilding.dto.project;

import com.skhu.gdgocteambuildingproject.global.pagination.PageInfo;
import java.util.List;
import lombok.Builder;

@Builder
public record ProjectInfoPageResponseDto(
        List<ProjectInfoResponseDto> projects,
        PageInfo pageInfo
) {
}
