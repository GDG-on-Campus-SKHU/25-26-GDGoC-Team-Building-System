package com.skhu.gdgocteambuildingproject.admin.dto.idea;

import lombok.Builder;

@Builder
public record IdeaTitleInfoIncludeDeletedResponseDto(
    long ideaId,
    String title,
    String introduction,
    int currentMemberCount,
    int maxMemberCount,
    boolean deleted
) {
}
