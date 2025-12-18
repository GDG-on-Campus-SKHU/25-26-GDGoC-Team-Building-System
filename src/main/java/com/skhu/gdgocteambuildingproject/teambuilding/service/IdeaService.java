package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.AdminIdeaDetailResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaConfigurationResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaTitleInfoIncludeDeletedPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaTextUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaDetailInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaTitleInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.RosterResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.TemporaryIdeaDetailResponseDto;

public interface IdeaService {

    TemporaryIdeaDetailResponseDto createIdea(
            long projectId,
            long userId,
            IdeaCreateRequestDto requestDto
    );

    IdeaConfigurationResponseDto findIdeaConfiguration(long userId);

    IdeaTitleInfoPageResponseDto findIdeas(
            long projectId,
            long userId,
            int page,
            int size,
            String sortBy,
            SortOrder order,
            boolean recruitingOnly,
            Long topicId
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
            long ideaId,
            long userId
    );

    AdminIdeaDetailResponseDto findIdeaDetailByAdmin(
            long projectId,
            long ideaId
    );

    TemporaryIdeaDetailResponseDto findTemporaryIdea(
            long projectId,
            long userId
    );

    RosterResponseDto getComposition(long userId);

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
