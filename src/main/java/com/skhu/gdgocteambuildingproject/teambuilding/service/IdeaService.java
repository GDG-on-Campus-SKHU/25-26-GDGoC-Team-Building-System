package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.admin.dto.idea.AdminIdeaDetailResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.idea.IdeaTitleInfoIncludeDeletedPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.IdeaTextUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.IdeaUpdateRequestDto;
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

    IdeaTitleInfoIncludeDeletedPageResponseDto findIdeasIncludeDeleted(
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

    AdminIdeaDetailResponseDto findIdeaDetailByAdmin(
            long projectId,
            long ideaId
    );

    IdeaDetailInfoResponseDto findTemporaryIdea(
            long projectId,
            long userId
    );

    void updateTexts(
            long projectId,
            long ideaId,
            long userId,
            IdeaTextUpdateRequestDto requestDto
    );

    void updateBeforeEnrollment(
            long projectId,
            long ideaId,
            long userId,
            IdeaUpdateRequestDto requestDto
    );

    void updateIdeaByAdmin(
            long ideaId,
            IdeaUpdateRequestDto requestDto
    );

    void softDeleteIdea(
            long projectId,
            long ideaId,
            long userId
    );

    void hardDeleteIdea(
            long ideaId
    );

    void removeMemberByAdmin(
            long ideaId,
            long memberId
    );

    void restoreIdea(
            long ideaId
    );

    void removeMember(
            long creatorId,
            long ideaId,
            long memberId
    );
}
