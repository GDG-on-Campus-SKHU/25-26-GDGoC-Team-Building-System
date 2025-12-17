package com.skhu.gdgocteambuildingproject.teambuilding.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ENROLLMENT_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_CONTENTS_EMPTY;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_TOTAL_MEMBER_COUNT_EXCEEDED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ILLEGAL_ENROLLMENT_STATUS;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ILLEGAL_PROJECT;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.CREATOR_CANNOT_BE_REMOVED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.NOT_CREATOR_OF_IDEA;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.NOT_MEMBER_OF_IDEA;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.NOT_REGISTRATION_SCHEDULE;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.TOPIC_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.REGISTERED_IDEA_ALREADY_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_PASSED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.TEMPORARY_IDEA_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.USER_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.TemporaryIdeaDetailResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.model.mapper.TemporaryIdeaMapper;
import java.time.LocalDateTime;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.EnrollmentStatus;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.IdeaStatus;
import com.skhu.gdgocteambuildingproject.teambuilding.model.mapper.IdeaDetailInfoMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.mapper.IdeaTitleInfoMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.mapper.RosterMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.IdeaEnrollmentRepository;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.IdeaRepository;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.IdeaMemberRepository;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.AdminIdeaDetailResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaTitleInfoIncludeDeletedPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaTitleInfoIncludeDeletedResponseDto;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.global.pagination.PageInfo;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectTopic;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaMemberCompositionRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaTextUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaDetailInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaTitleInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaTitleInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.RosterResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ParticipationUtil;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ProjectUtil;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.TeamBuildingProjectRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class IdeaServiceImpl implements IdeaService {

    private final IdeaRepository ideaRepository;
    private final IdeaMemberRepository ideaMemberRepository;
    private final TeamBuildingProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IdeaEnrollmentRepository ideaEnrollmentRepository;
    private final EntityManager entityManager;

    private final ProjectUtil projectUtil;
    private final ParticipationUtil participationUtil;

    private final IdeaTitleInfoMapper ideaTitleInfoMapper;
    private final IdeaDetailInfoMapper ideaDetailInfoMapper;
    private final TemporaryIdeaMapper temporaryIdeaMapper;
    private final RosterMapper rosterMapper;

    @Override
    @Transactional
    public TemporaryIdeaDetailResponseDto createIdea(
            long projectId,
            long userId,
            IdeaCreateRequestDto requestDto
    ) {
        participationUtil.validateParticipation(userId, projectId);

        ProjectSchedule currentSchedule = findCurrentSchedule();
        validateRegistrationSchedule(currentSchedule);

        TeamBuildingProject project = findProjectBy(projectId);

        if (requestDto.registerStatus() == IdeaStatus.REGISTERED) {
            validateIdeaTexts(requestDto.getTexts());
            validateTotalMemberCount(requestDto.compositions(), project);
        }

        User creator = findUserBy(userId);

        Idea postedIdea = ideaRepository.findByCreatorAndProject(creator, project)
                .map(idea -> updateExistTemporaryIdea(idea, requestDto))
                .orElseGet(() -> saveNewIdea(creator, project, requestDto));

        return temporaryIdeaMapper.map(postedIdea);
    }

    @Override
    @Transactional(readOnly = true)
    public IdeaTitleInfoPageResponseDto findIdeas(
            long projectId,
            long userId,
            int page,
            int size,
            String sortBy,
            SortOrder order,
            boolean recruitingOnly,
            Long topicId
    ) {
        participationUtil.validateParticipation(userId, projectId);

        Pageable pagination = setupPagination(page, size, sortBy, order);

        Page<Idea> ideas = findIdeasBy(projectId, recruitingOnly, topicId, pagination);
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
            long ideaId,
            long userId
    ) {
        participationUtil.validateParticipation(userId, projectId);

        Idea idea = ideaRepository.findByIdAndProjectId(ideaId, projectId)
                .orElseThrow(() -> new IllegalArgumentException(IDEA_NOT_EXIST.getMessage()));

        return ideaDetailInfoMapper.map(idea);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminIdeaDetailResponseDto findIdeaDetailByAdmin(
            long projectId,
            long ideaId
    ) {
        Idea idea = ideaRepository.findByIdIncludeDeleted(ideaId)
                .orElseThrow(() -> new IllegalArgumentException(IDEA_NOT_EXIST.getMessage()));

        validateIdeaInProject(idea, projectId);

        return ideaDetailInfoMapper.mapForAdmin(idea);
    }

    @Override
    @Transactional(readOnly = true)
    public TemporaryIdeaDetailResponseDto findTemporaryIdea(
            long projectId,
            long userId
    ) {
        participationUtil.validateParticipation(userId, projectId);

        Idea idea = ideaRepository.findByCreatorIdAndProjectId(userId, projectId)
                .filter(Idea::isTemporary)
                .orElseThrow(() -> new EntityNotFoundException(TEMPORARY_IDEA_NOT_EXIST.getMessage()));

        return temporaryIdeaMapper.map(idea);
    }

    @Override
    @Transactional(readOnly = true)
    public RosterResponseDto getComposition(long userId) {
        User user = findUserBy(userId);
        TeamBuildingProject currentProject = findCurrentProject();
        participationUtil.validateParticipation(userId, currentProject.getId());

        Idea idea = user.getIdeaFrom(currentProject)
                .orElseThrow(() -> new EntityNotFoundException(IDEA_NOT_EXIST.getMessage()));

        return rosterMapper.map(user, idea);
    }

    @Override
    @Transactional
    public void updateTexts(
            long projectId,
            long ideaId,
            long userId,
            IdeaTextUpdateRequestDto requestDto
    ) {
        participationUtil.validateParticipation(userId, projectId);

        validateIdeaTexts(requestDto.getTexts());

        Idea idea = findRegisteredIdeaBy(ideaId);

        validateIdeaInProject(idea, projectId);
        validateIdeaOwnership(idea, userId);

        ProjectTopic topic = findTopicBy(idea.getProject(), requestDto.topicId());
        idea.updateTopic(topic);
        idea.updateTexts(
                requestDto.title(),
                requestDto.introduction(),
                requestDto.description()
        );
    }

    @Override
    @Transactional
    public void updateBeforeEnrollment(
            long projectId,
            long ideaId,
            long userId,
            IdeaUpdateRequestDto requestDto
    ) {
        participationUtil.validateParticipation(userId, projectId);

        validateIdeaTexts(requestDto.getTexts());

        Idea idea = findRegisteredIdeaBy(ideaId);

        validateIdeaInProject(idea, projectId);
        validateIdeaOwnership(idea, userId);
        validateBeforeEnrollment(idea);
        validateTotalMemberCount(requestDto.compositions(), idea.getProject());

        ProjectTopic topic = findTopicBy(idea.getProject(), requestDto.topicId());
        idea.updateTopic(topic);
        idea.updateTexts(
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

        ProjectTopic topic = findTopicBy(idea.getProject(), requestDto.topicId());
        idea.updateTopic(topic);
        idea.updateTexts(
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
        participationUtil.validateParticipation(userId, projectId);

        Idea idea = ideaRepository.findByIdAndCreatorIdAndProjectId(ideaId, userId, projectId)
                .orElseThrow(() -> new EntityNotFoundException(IDEA_NOT_EXIST.getMessage()));

        validateIdeaDeletable(idea);

        // 소프트 딜리트를 사용하므로 명시적으로 제거
        ideaMemberRepository.deleteAllByIdeaId(ideaId);
        ideaEnrollmentRepository.deleteAllByIdeaId(ideaId);
        entityManager.flush();

        idea.markAsDeleted();
    }

    @Override
    @Transactional
    public void hardDeleteIdea(long ideaId) {
        Idea idea = findIdeaIncludeDeleted(ideaId);
        ideaRepository.delete(idea);
    }

    @Override
    @Transactional
    public void removeMemberByAdmin(long ideaId, long memberId) {
        Idea idea = findIdeaIncludeDeleted(ideaId);
        User member = findUserBy(memberId);

        validateRemovableMember(idea, member);

        // 이미 수락된 멤버면 제거
        boolean isConfirmedMember = idea.containsAsMember(member);
        if (isConfirmedMember) {
            idea.removeMember(member);
            return;
        }

        // 수락 예정인 멤버면 스케줄러가 무시하도록 상태 변경
        IdeaEnrollment enrollment = idea.findScheduleToAcceptEnrollmentOf(member)
                .orElseThrow(() -> new IllegalStateException(NOT_MEMBER_OF_IDEA.getMessage()));
        enrollment.scheduleToReject();
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

    @Override
    @Transactional
    public void removeMember(
            long creatorId,
            long ideaId,
            long memberId
    ) {
        Idea idea = findIdeaBy(ideaId);
        TeamBuildingProject project = idea.getProject();
        participationUtil.validateParticipation(creatorId, project.getId());

        validateIdeaOwnership(idea, creatorId);

        User member = findUserBy(memberId);
        IdeaEnrollment enrollment = findEnrollmentOf(idea, member);

        validateMemberDeletable(enrollment);

        enrollment.scheduleToReject();
    }

    private ProjectSchedule findCurrentSchedule() {
        return projectUtil.findCurrentSchedule()
                .orElseThrow(() -> new EntityNotFoundException(SCHEDULE_NOT_EXIST.getMessage()));
    }

    private TeamBuildingProject findCurrentProject() {
        return projectUtil.findCurrentProject()
                .orElseThrow(() -> new IllegalStateException(PROJECT_NOT_EXIST.getMessage()));
    }

    private void validateIdeaDeletable(Idea idea) {
        // 멤버가 없는 아이디어라면 일정에 상관 없이 삭제 가능
        if (!idea.hasMember()) {
            return;
        }

        ProjectSchedule currentSchedule = findCurrentSchedule();
        TeamBuildingProject project = currentSchedule.getProject();

        if (!project.equals(idea.getProject())) {
            throw new IllegalStateException(SCHEDULE_PASSED.getMessage());
        }

        if (!currentSchedule.getType().isIdeaDeletable()) {
            throw new IllegalStateException(SCHEDULE_PASSED.getMessage());
        }
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

    private Page<Idea> findIdeasBy(
            long projectId,
            boolean recruitingOnly,
            Long topicId,
            Pageable pagination
    ) {
        if (recruitingOnly) {
            return findRecruitingIdeas(projectId, topicId, pagination);
        }

        return findAllIdeas(projectId, topicId, pagination);
    }

    private Page<Idea> findRecruitingIdeas(
            long projectId,
            Long topicId,
            Pageable pagination
    ) {
        if (topicId == null) {
            return ideaRepository.findByProjectIdAndRecruitingIsTrue(projectId, pagination);
        }

        return ideaRepository.findByProjectIdAndTopicIdAndRecruitingIsTrue(projectId, topicId, pagination);
    }

    private Page<Idea> findAllIdeas(
            long projectId,
            Long topicId,
            Pageable pagination
    ) {
        if (topicId == null) {
            return ideaRepository.findByProjectId(projectId, pagination);
        }

        return ideaRepository.findByProjectIdAndTopicId(projectId, topicId, pagination);
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
        ProjectSchedule currentSchedule = findCurrentSchedule();
        TeamBuildingProject currentProject = currentSchedule.getProject();

        if (!currentProject.equals(idea.getProject())) {
            throw new IllegalStateException(SCHEDULE_PASSED.getMessage());
        }

        if (currentSchedule.getType() != ScheduleType.IDEA_REGISTRATION) {
            throw new IllegalStateException(SCHEDULE_PASSED.getMessage());
        }
    }

    private void validateMemberDeletable(IdeaEnrollment enrollment) {
        if (enrollment.getStatus() != EnrollmentStatus.SCHEDULED_TO_ACCEPT) {
            throw new IllegalStateException(ILLEGAL_ENROLLMENT_STATUS.getMessage());
        }
    }

    private Idea buildIdea(
            IdeaCreateRequestDto requestDto,
            TeamBuildingProject project,
            User creator
    ) {
        Idea idea = Idea.builder()
                .title(requestDto.title())
                .introduction(requestDto.introduction())
                .description(requestDto.description())
                .registerStatus(requestDto.registerStatus())
                .project(project)
                .creator(creator)
                .creatorPart(requestDto.creatorPart())
                .build();
        idea.initCreatorToMember(requestDto.creatorPart());

        switch (requestDto.registerStatus()) {
            case REGISTERED -> updateTopic(idea, requestDto.topicId());
            case TEMPORARY -> {
                idea.updateLastTemporarySavedAt(LocalDateTime.now());
                updateTopicIfExists(idea, requestDto.topicId());
            }
        }

        return idea;
    }

    private Idea updateExistTemporaryIdea(
            Idea idea,
            IdeaCreateRequestDto requestDto
    ) {
        validateIdeaNotRegistered(idea);

        switch (requestDto.registerStatus()) {
            case REGISTERED -> {
                validateTotalMemberCount(requestDto.compositions(), idea.getProject());
                updateTopic(idea, requestDto.topicId());
                idea.register();
            }
            case TEMPORARY -> {
                idea.updateLastTemporarySavedAt(LocalDateTime.now());
                updateTopicIfExists(idea, requestDto.topicId());
            }
        }

        idea.updateTexts(
                requestDto.title(),
                requestDto.introduction(),
                requestDto.description()
        );
        idea.updateCreatorPart(requestDto.creatorPart());
        updateIdeaCompositions(idea, requestDto.compositions());

        return idea;
    }

    private void updateTopicIfExists(Idea idea, Long topicId) {
        if (topicId == null) {
            return;
        }

        updateTopic(idea, topicId);
    }

    private void updateTopic(Idea idea, Long topicId) {
        if (topicId == null) {
            throw new IllegalArgumentException(TOPIC_NOT_EXIST.getMessage());
        }

        ProjectTopic topic = findTopicBy(idea.getProject(), topicId);
        idea.updateTopic(topic);
    }

    private void updateIdeaCompositions(
            Idea idea,
            List<IdeaMemberCompositionRequestDto> compositions
    ) {
        List<IdeaMemberCompositionRequestDto> recruitingCompositions = compositions.stream()
                .filter(composition -> composition.maxCount() > 0)
                .toList();

        for (IdeaMemberCompositionRequestDto composition : recruitingCompositions) {
            Part part = composition.part();
            int maxCount = composition.maxCount();

            idea.updateComposition(part, maxCount);
        }
    }

    private Idea findIdeaBy(long ideaId) {
        return ideaRepository.findById(ideaId)
                .orElseThrow(() -> new EntityNotFoundException(IDEA_NOT_EXIST.getMessage()));
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

    private IdeaEnrollment findEnrollmentOf(Idea idea, User member) {
        return idea.getEnrollments().stream()
                .filter(enrollment -> enrollment.getApplicant().equals(member))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException(ENROLLMENT_NOT_EXIST.getMessage()));
    }

    private ProjectTopic findTopicBy(TeamBuildingProject project, Long topicId) {
        return project.getTopics().stream()
                .filter(topic -> topic.getId().equals(topicId))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException(TOPIC_NOT_EXIST.getMessage()));
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

    private void validateTotalMemberCount(
            List<IdeaMemberCompositionRequestDto> compositions,
            TeamBuildingProject project
    ) {
        int totalMemberCount = compositions.stream()
                .mapToInt(IdeaMemberCompositionRequestDto::maxCount)
                .sum();

        int projectMaxMemberCount = project.getMaxMemberCount();

        if (totalMemberCount > projectMaxMemberCount) {
            throw new IllegalStateException(IDEA_TOTAL_MEMBER_COUNT_EXCEEDED.getMessage());
        }
    }

    private void validateRemovableMember(Idea idea, User user) {
        if (idea.isCreator(user)) {
            throw new IllegalStateException(CREATOR_CANNOT_BE_REMOVED.getMessage());
        }
    }
}
