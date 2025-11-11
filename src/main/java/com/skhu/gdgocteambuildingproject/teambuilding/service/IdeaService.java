package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.IdeaDetailInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.IdeaTitleInfoPageResponseDto;

public interface IdeaService {
    IdeaTitleInfoPageResponseDto findIdeas(
            long projectId,
            int page,
            int size,
            String sortBy,
            SortOrder order
    );

    IdeaDetailInfoResponseDto findIdeaDetail(
            long projectId,
            long ideaId
    );
}
