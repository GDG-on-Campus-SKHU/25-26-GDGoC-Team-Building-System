package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.IdeaCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaDetailInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaTitleInfoPageResponseDto;

public interface IdeaService {

    IdeaDetailInfoResponseDto createIdea(
            long projectId,
            long userId,
            IdeaCreateRequestDto requestDto
    );

    IdeaTitleInfoPageResponseDto findIdeas(
            long projectId,
            int page,
            int size,
            String sortBy,
            SortOrder order,
            boolean recruitingOnly
    );

    IdeaDetailInfoResponseDto findIdeaDetail(
            long projectId,
            long ideaId
    );
}
