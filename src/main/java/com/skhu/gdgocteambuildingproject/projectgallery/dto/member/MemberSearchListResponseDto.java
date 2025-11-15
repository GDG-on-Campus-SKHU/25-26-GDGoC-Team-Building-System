package com.skhu.gdgocteambuildingproject.projectgallery.dto.member;

import lombok.Builder;

import java.util.List;

@Builder
public record MemberSearchListResponseDto(
        List<MemberSearchResponseDto> members
) {
}
