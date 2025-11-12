package com.skhu.gdgocteambuildingproject.teambuilding.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.repository.IdeaRepository;
import com.skhu.gdgocteambuildingproject.global.pagination.PageInfo;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.IdeaDetailInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.IdeaTitleInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.IdeaTitleInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.model.IdeaDetailInfoMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.IdeaTitleInfoMapper;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class IdeaServiceImpl implements IdeaService {

    private final IdeaRepository ideaRepository;

    private final IdeaTitleInfoMapper ideaTitleInfoMapper;
    private final IdeaDetailInfoMapper ideaDetailInfoMapper;

    @Override
    @Transactional(readOnly = true)
    public IdeaTitleInfoPageResponseDto findIdeas(
            long projectId,
            int page,
            int size,
            String sortBy,
            SortOrder order
    ) {
        Pageable pagination = setupPagination(page, size, sortBy, order);

        Page<Idea> ideas = ideaRepository.findByProjectId(projectId, pagination);
        List<IdeaTitleInfoResponseDto> ideaDtos = ideas
                .stream()
                .map(ideaTitleInfoMapper::map)
                .toList();

        return IdeaTitleInfoPageResponseDto.builder()
                .ideas(ideaDtos)
                .pageInfo(PageInfo.from(ideas))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public IdeaDetailInfoResponseDto findIdeaDetail(
            long projectId,
            long ideaId
    ) {
        Idea idea = ideaRepository.findByIdAndProjectId(ideaId, projectId)
                .orElseThrow(() -> new IllegalArgumentException(IDEA_NOT_EXIST.getMessage()));

        return ideaDetailInfoMapper.map(idea);
    }

    private Pageable setupPagination(
            int page,
            int size,
            String sortBy,
            SortOrder order
    ) {
        return PageRequest.of(
                page,
                size,
                order.sort(sortBy)
        );
    }
}
