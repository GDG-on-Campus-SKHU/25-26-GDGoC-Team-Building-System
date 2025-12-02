package com.skhu.gdgocteambuildingproject.teambuilding.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_CONTENTS_EMPTY;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ILLEGAL_PROJECT;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.NOT_CREATOR_OF_IDEA;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.NOT_REGISTRATION_SCHEDULE;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.REGISTERED_IDEA_ALREADY_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_PASSED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.TEMPORARY_IDEA_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.USER_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaStatus;
import com.skhu.gdgocteambuildingproject.Idea.repository.IdeaRepository;
import com.skhu.gdgocteambuildingproject.admin.dto.idea.IdeaTitleInfoIncludeDeletedPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.idea.IdeaTitleInfoIncludeDeletedResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.idea.IdeaUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.global.pagination.PageInfo;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.IdeaCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.IdeaMemberCompositionRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaDetailInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaTitleInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaTitleInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.model.IdeaDetailInfoMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.IdeaTitleInfoMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ProjectUtil;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.TeamBuildingProjectRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
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

    private final ProjectUtil projectUtil;
    private final IdeaTitleInfoMapper ideaTitleInfoMapper;
    private final IdeaDetailInfoMapper ideaDetailInfoMapper;

    @Override
    @Transactional
    public IdeaDetailInfoResponseDto createIdea(
            long projectId,
            long userId,
            IdeaCreateRequestDto requestDto
    ) {
        ProjectSchedule currentSchedule = getCurrentSchedule();
        validateRegistrationSchedule(currentSchedule);

        if (requestDto.registerStatus() == IdeaStatus.REGISTERED) {
            validateIdeaTexts(requestDto.getTexts());
        }

        TeamBuildingProject project = findProjectBy(projectId);
        User creator = findUserBy(userId);

        Idea postedIdea = ideaRepository.findByCreatorAndProject(creator, project)
                .map(idea -> updateExistTemporaryIdea(idea, requestDto))
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
    public IdeaTitleInfoIncludeDeletedPageResponseDto findIdeasIncludeDeleted(
            long projectId,
            int page,
            int size,
            String sortBy,
            SortOrder order
    ) {
        Pageable pagination = setupPagination(page, size, sortBy, order);

        Page<Idea> ideas = ideaRepository.findByProjectIdIncludeDeleted(projectId, pagination);

        List<IdeaTitleInfoIncludeDeletedResponseDto> ideaDtos = ideas.stream()
                .map(ideaTitleInfoMapper::mapIncludeDeleted)
                .toList();

        return IdeaTitleInfoIncludeDeletedPageResponseDto.builder()
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
    public void updateBeforeEnrollment(
            long projectId,
            long ideaId,
            long userId,
            IdeaUpdateRequestDto requestDto
    ) {
        validateIdeaTexts(requestDto.getTexts());

        Idea idea = findRegisteredIdeaBy(ideaId);

        validateIdeaInProject(idea, projectId);
        validateIdeaOwnership(idea, userId);
        validateBeforeEnrollment(idea);

        idea.updateTexts(
                requestDto.topic(),
                requestDto.title(),
                requestDto.introduction(),
                requestDto.description()
        );
        idea.updateCreatorPart(requestDto.creatorPart());
        updateIdeaCompositions(idea, requestDto.compositions());
    }

    @Override
    @Transactional
    public void updateIdeaByAdmin(
            long ideaId,
            IdeaUpdateRequestDto requestDto
    ) {
        validateIdeaTexts(requestDto.getTexts());

        Idea idea = findIdeaIncludeDeleted(ideaId);

        idea.updateTexts(
                requestDto.topic(),
                requestDto.title(),
                requestDto.introduction(),
                requestDto.description()
        );
        idea.updateCreatorPart(requestDto.creatorPart());
        updateIdeaCompositions(idea, requestDto.compositions());
    }

    @Override
    @Transactional
    public void softDeleteIdea(
            long projectId,
            long ideaId,
            long userId
    ) {
        Idea idea = ideaRepository.findByIdAndCreatorIdAndProjectId(ideaId, userId, projectId)
                .orElseThrow(() -> new EntityNotFoundException(IDEA_NOT_EXIST.getMessage()));

        idea.delete();
    }

    @Override
    @Transactional
    public void hardDeleteIdea(long ideaId) {
        ideaRepository.deleteById(ideaId);
    }

    @Override
    @Transactional
    public void restoreIdea(long ideaId) {
        Idea deletedIdea = findDeletedIdea(ideaId);
        User creator = deletedIdea.getCreator();
        validateRestorable(deletedIdea);

        deleteTemporaryIdeaIfExist(creator, deletedIdea.getProject());
        deletedIdea.restore();
    }

    private ProjectSchedule getCurrentSchedule() {
        return projectUtil.findCurrentSchedule()
                .orElseThrow(() -> new EntityNotFoundException(SCHEDULE_NOT_EXIST.getMessage()));
    }

    private void validateRegistrationSchedule(ProjectSchedule schedule) {
        if (schedule.getType() != ScheduleType.IDEA_REGISTRATION) {
            throw new IllegalStateException(NOT_REGISTRATION_SCHEDULE.getMessage());
        }
    }

    private void validateIdeaTexts(List<String> texts) {
        boolean anyBlank = texts.stream()
                .anyMatch(String::isBlank);

        if (anyBlank) {
            throw new IllegalArgumentException(IDEA_CONTENTS_EMPTY.getMessage());
        }
    }

    private void validateRestorable(Idea deletedIdea) {
        User creator = deletedIdea.getCreator();
        TeamBuildingProject project = deletedIdea.getProject();

        boolean registeredIdeaExist = creator.getIdeas().stream()
                .filter(idea -> project.equals(idea.getProject()))
                .anyMatch(Idea::isRegistered);

        if (registeredIdeaExist) {
            throw new IllegalStateException(REGISTERED_IDEA_ALREADY_EXIST.getMessage());
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

    private void validateIdeaNotRegistered(Idea idea) {
        if (idea.getRegisterStatus() == IdeaStatus.REGISTERED) {
            throw new IllegalStateException(REGISTERED_IDEA_ALREADY_EXIST.getMessage());
        }
    }

    private void validateIdeaInProject(Idea idea, long projectId) {
        Long actualProjectId = idea.getProject().getId();

        if (!actualProjectId.equals(projectId)) {
            throw new IllegalStateException(ILLEGAL_PROJECT.getMessage());
        }
    }

    private void validateIdeaOwnership(Idea idea, long userId) {
        Long creatorId = idea.getCreator().getId();

        if (!creatorId.equals(userId)) {
            throw new IllegalStateException(NOT_CREATOR_OF_IDEA.getMessage());
        }
    }

    private void validateBeforeEnrollment(Idea idea) {
        ProjectSchedule currentSchedule = getCurrentSchedule();
        TeamBuildingProject currentProject = currentSchedule.getProject();

        if (!currentProject.equals(idea.getProject())) {
            throw new IllegalStateException(SCHEDULE_PASSED.getMessage());
        }

        if (currentSchedule.getType() != ScheduleType.IDEA_REGISTRATION) {
            throw new IllegalStateException(SCHEDULE_PASSED.getMessage());
        }
    }

    private Idea buildIdea(
            IdeaCreateRequestDto ideaDto,
            TeamBuildingProject project,
            User creator
    ) {
        Idea idea = Idea.builder()
                .topic(ideaDto.topic())
                .title(ideaDto.title())
                .introduction(ideaDto.introduction())
                .description(ideaDto.description())
                .registerStatus(ideaDto.registerStatus())
                .project(project)
                .creator(creator)
                .creatorPart(ideaDto.creatorPart())
                .build();
        idea.initCreatorToMember(ideaDto.creatorPart());

        return idea;
    }

    private Idea updateExistTemporaryIdea(
            Idea idea,
            IdeaCreateRequestDto requestDto
    ) {
        validateIdeaNotRegistered(idea);

        idea.updateTexts(
                requestDto.topic(),
                requestDto.title(),
                requestDto.introduction(),
                requestDto.description()
        );
        idea.updateCreatorPart(requestDto.creatorPart());
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

    private Idea findRegisteredIdeaBy(long ideaId) {
        return ideaRepository.findByIdAndRegisterStatus(ideaId, IdeaStatus.REGISTERED)
                .orElseThrow(() -> new EntityNotFoundException(IDEA_NOT_EXIST.getMessage()));
    }

    private Idea findDeletedIdea(long ideaId) {
        return ideaRepository.findDeletedIdeaById(ideaId)
                .orElseThrow(() -> new EntityNotFoundException(IDEA_NOT_EXIST.getMessage()));
    }

    private Idea findIdeaIncludeDeleted(long ideaId) {
        return ideaRepository.findByIdIncludeDeleted(ideaId)
                .orElseThrow(() -> new EntityNotFoundException(IDEA_NOT_EXIST.getMessage()));
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

    private void deleteTemporaryIdeaIfExist(User user, TeamBuildingProject project) {
        user.getIdeas().stream()
                .filter(idea -> project.equals(idea.getProject()))
                .filter(Idea::isTemporary)
                .findAny()
                .ifPresent(user::removeIdea);
    }
}
