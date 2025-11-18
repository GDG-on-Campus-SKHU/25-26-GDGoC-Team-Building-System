package com.skhu.gdgocteambuildingproject.teambuilding.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_CONTENTS_EMPTY;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.TEMPORARY_IDEA_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.REGISTERED_IDEA_ALREADY_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.USER_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaStatus;
import com.skhu.gdgocteambuildingproject.Idea.repository.IdeaRepository;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.global.pagination.PageInfo;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.IdeaCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.IdeaMemberCompositionRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaDetailInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaTitleInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaTitleInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.model.IdeaDetailInfoMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.IdeaTitleInfoMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.TeamBuildingProjectRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
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
    private final TeamBuildingProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final IdeaTitleInfoMapper ideaTitleInfoMapper;
    private final IdeaDetailInfoMapper ideaDetailInfoMapper;

    @Override
    @Transactional
    public IdeaDetailInfoResponseDto createIdea(
            long projectId,
            long userId,
            IdeaCreateRequestDto requestDto
    ) {
        if (requestDto.registerStatus() == IdeaStatus.REGISTERED) {
            validateContents(requestDto);
        }

        TeamBuildingProject project = findProjectBy(projectId);
        User creator = findUserBy(userId);

        Idea postedIdea = ideaRepository.findByCreatorAndProject(creator, project)
                .map(idea -> updateExistIdea(idea, requestDto))
                .orElseGet(() -> saveNewIdea(creator, project, requestDto));

        return ideaDetailInfoMapper.map(postedIdea);
    }

    @Override
    @Transactional(readOnly = true)
    public IdeaTitleInfoPageResponseDto findIdeas(
            long projectId,
            int page,
            int size,
            String sortBy,
            SortOrder order,
            boolean recruitingOnly
    ) {
        Pageable pagination = setupPagination(page, size, sortBy, order);

        Page<Idea> ideas = findPagedIdeasOf(projectId, pagination, recruitingOnly);
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

    @Override
    @Transactional(readOnly = true)
    public IdeaDetailInfoResponseDto findTemporaryIdea(
            long projectId,
            long userId
    ) {
        Idea idea = ideaRepository.findByCreatorIdAndProjectId(userId, projectId)
                .filter(Idea::isTemporary)
                .orElseThrow(() -> new EntityNotFoundException(TEMPORARY_IDEA_NOT_EXIST.getMessage()));

        return ideaDetailInfoMapper.map(idea);
    }

    @Override
    @Transactional
    public void deleteIdea(
            long projectId,
            long ideaId,
            long userId
    ) {
        Idea idea = ideaRepository.findByIdAndCreatorIdAndProjectId(ideaId, userId, projectId)
                .orElseThrow(() -> new EntityNotFoundException(IDEA_NOT_EXIST.getMessage()));

        idea.delete();
    }

    private void validateContents(IdeaCreateRequestDto requestDto) {
        if (StringUtils.isBlank(requestDto.title())
                || StringUtils.isBlank(requestDto.introduction())
                || StringUtils.isBlank(requestDto.description())
                || StringUtils.isBlank(requestDto.topic())
        ) {
            throw new IllegalArgumentException(IDEA_CONTENTS_EMPTY.getMessage());
        }
    }

    private Page<Idea> findPagedIdeasOf(long projectId, Pageable pagination, boolean recruitingOnly) {
        if (recruitingOnly) {
            return ideaRepository.findByProjectIdAndRecruitingIsTrue(projectId, pagination);
        }

        return ideaRepository.findByProjectId(projectId, pagination);
    }

    private Idea saveNewIdea(
            User creator,
            TeamBuildingProject project,
            IdeaCreateRequestDto requestDto
    ) {
        Idea idea = buildIdea(requestDto, project, creator);
        updateIdeaCompositions(idea, requestDto.compositions());
        creator.addIdea(idea);

        return ideaRepository.save(idea);
    }

    private void validateIdeaModifiable(Idea idea) {
        if (idea.getRegisterStatus() != IdeaStatus.TEMPORARY) {
            throw new IllegalStateException(REGISTERED_IDEA_ALREADY_EXIST.getMessage());
        }
    }

    private Idea buildIdea(
            IdeaCreateRequestDto ideaDto,
            TeamBuildingProject project,
            User creator
    ) {
        return Idea.builder()
                .topic(ideaDto.topic())
                .title(ideaDto.title())
                .introduction(ideaDto.introduction())
                .description(ideaDto.description())
                .registerStatus(ideaDto.registerStatus())
                .project(project)
                .creator(creator)
                .build();
    }

    private Idea updateExistIdea(
            Idea idea,
            IdeaCreateRequestDto requestDto
    ) {
        validateIdeaModifiable(idea);

        idea.updateTexts(
                requestDto.topic(),
                requestDto.title(),
                requestDto.introduction(),
                requestDto.description()
        );
        updateIdeaCompositions(idea, requestDto.compositions());

        if (requestDto.registerStatus() == IdeaStatus.REGISTERED) {
            idea.register();
        }

        return idea;
    }

    private void updateIdeaCompositions(
            Idea idea,
            List<IdeaMemberCompositionRequestDto> compositions
    ) {
        for (IdeaMemberCompositionRequestDto composition : compositions) {
            Part part = composition.part();
            int maxCount = composition.maxCount();

            idea.updateComposition(part, maxCount);
        }
    }

    private TeamBuildingProject findProjectBy(long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXIST.getMessage()));
    }

    private User findUserBy(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXIST.getMessage()));
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
