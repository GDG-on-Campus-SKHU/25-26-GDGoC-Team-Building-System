package com.skhu.gdgocteambuildingproject.teambuilding.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ALREADY_ENROLL;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.CHOICE_NOT_AVAILABLE;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ENROLLMENT_NOT_AVAILABLE;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ENROLLMENT_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_CREATOR_CANNOT_ENROLL;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.NOT_CREATOR_OF_IDEA;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.REGISTERED_IDEA_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_PASSED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.USER_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaStatus;
import com.skhu.gdgocteambuildingproject.Idea.repository.IdeaRepository;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.Choice;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.EnrollmentDetermineRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.EnrollmentRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.CompositionResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.EnrollmentAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.ReceivedEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.SentEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.model.CompositionMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.EnrollmentAvailabilityMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ProjectUtil;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ReceivedEnrollmentMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.SentEnrollmentMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.IdeaEnrollmentRepository;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.TeamBuildingProjectRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;
    private final TeamBuildingProjectRepository projectRepository;
    private final IdeaEnrollmentRepository enrollmentRepository;

    private final EnrollmentAvailabilityMapper availabilityMapper;
    private final SentEnrollmentMapper sentEnrollmentMapper;
    private final ReceivedEnrollmentMapper receivedEnrollmentMapper;
    private final CompositionMapper compositionMapper;
    private final ProjectUtil projectUtil;

    @Override
    @Transactional
    public void enroll(
            long applicantId,
            long ideaId,
            EnrollmentRequestDto requestDto
    ) {
        User applicant = findUserBy(applicantId);
        Idea idea = findIdeaBy(ideaId);

        TeamBuildingProject currentProject = findCurrentProject();
        ProjectSchedule currentSchedule = findCurrentSchedule(currentProject);

        validateEnrollmentAvailable(currentSchedule.getType());
        validateChoice(requestDto.choice(), applicant, currentSchedule);
        validateApplicantCanEnroll(applicant, currentProject);
        validateEnrollmentUnique(applicant, idea);

        IdeaEnrollment enrollment = saveEnrollment(applicant, idea, currentSchedule, requestDto);
        idea.addEnrollment(enrollment);
        applicant.addEnrollment(enrollment);
    }

    @Override
    @Transactional
    public void determineEnrollment(
            long userId,
            long enrollmentId,
            EnrollmentDetermineRequestDto requestDto
    ) {
        User creator = findUserBy(userId);
        IdeaEnrollment enrollment = findEnrollmentWithLock(enrollmentId);
        Idea idea = enrollment.getIdea();

        TeamBuildingProject currentProject = findCurrentProject();
        ProjectSchedule currentSchedule = findCurrentSchedule(currentProject);

        validateIdeaOwnership(creator, idea);
        validateInSchedule(enrollment, currentSchedule);

        if (requestDto.accept()) {
            idea.acceptEnrollment(enrollment);
        } else {
            idea.rejectEnrollment(enrollment);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CompositionResponseDto getComposition(long userId) {
        User user = findUserBy(userId);
        TeamBuildingProject currentProject = findCurrentProject();
        Idea idea = user.getIdeaFrom(currentProject)
                .orElseThrow(() -> new EntityNotFoundException(IDEA_NOT_EXIST.getMessage()));

        return compositionMapper.map(user, idea);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentAvailabilityResponseDto getAvailabilityInfo(
            long ideaId,
            long applicantId
    ) {
        Idea idea = findIdeaBy(ideaId);
        TeamBuildingProject project = idea.getProject();
        ProjectSchedule currentSchedule = findCurrentSchedule(project);
        User applicant = findUserBy(applicantId);

        validateEnrollmentAvailable(currentSchedule.getType());

        return availabilityMapper.map(project, currentSchedule, idea, applicant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SentEnrollmentResponseDto> getSentEnrollments(
            long userId,
            ScheduleType scheduleType
    ) {
        validateEnrollmentAvailable(scheduleType);

        User user = findUserBy(userId);
        TeamBuildingProject currentProject = findCurrentProject();
        ProjectSchedule schedule = currentProject.getScheduleFrom(scheduleType);

        List<IdeaEnrollment> enrollments = user.getEnrollmentFrom(schedule);

        return enrollments.stream()
                .map(enrollment -> sentEnrollmentMapper.map(enrollment, schedule))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReceivedEnrollmentResponseDto> getReceivedEnrollments(
            long userId,
            ScheduleType scheduleType
    ) {
        validateEnrollmentAvailable(scheduleType);

        User user = findUserBy(userId);
        TeamBuildingProject currentProject = findCurrentProject();
        Idea idea = findRegisteredIdeaOf(user, currentProject);

        ProjectSchedule schedule = currentProject.getScheduleFrom(scheduleType);
        List<IdeaEnrollment> enrollments = idea.getEnrollmentsOf(schedule);

        return enrollments.stream()
                .map(enrollment -> receivedEnrollmentMapper.map(enrollment, idea))
                .toList();
    }

    private Idea findIdeaBy(long ideaId) {
        return ideaRepository.findById(ideaId)
                .orElseThrow(() -> new EntityNotFoundException(IDEA_NOT_EXIST.getMessage()));
    }

    private IdeaEnrollment findEnrollmentWithLock(long enrollmentId) {
        return enrollmentRepository.findByIdWithIdeaLock(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException(ENROLLMENT_NOT_EXIST.getMessage()));
    }

    private ProjectSchedule findCurrentSchedule(TeamBuildingProject project) {
        return project.getCurrentSchedule()
                .orElseThrow(() -> new IllegalStateException(ENROLLMENT_NOT_AVAILABLE.getMessage()));
    }

    private User findUserBy(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXIST.getMessage()));
    }

    private TeamBuildingProject findCurrentProject() {
        // 아직 마지막 일정이 끝나지 않은 프로젝트들만 조회
        List<TeamBuildingProject> unfinishedProjects = projectRepository.findProjectsWithScheduleNotEndedBefore(
                ScheduleType.FINAL_RESULT_ANNOUNCEMENT,
                LocalDateTime.now()
        );

        return projectUtil.findCurrentProject(unfinishedProjects)
                .orElseThrow(() -> new IllegalStateException(PROJECT_NOT_EXIST.getMessage()));
    }

    private Idea findRegisteredIdeaOf(User creator, TeamBuildingProject project) {
        return ideaRepository.findIdeaOf(
                creator.getId(),
                project.getId(),
                IdeaStatus.REGISTERED
        ).orElseThrow(() -> new EntityNotFoundException(REGISTERED_IDEA_NOT_EXIST.getMessage()));
    }

    private IdeaEnrollment saveEnrollment(
            User applicant,
            Idea idea,
            ProjectSchedule schedule,
            EnrollmentRequestDto requestDto
    ) {
        IdeaEnrollment enrollment = IdeaEnrollment.builder()
                .applicant(applicant)
                .idea(idea)
                .choice(requestDto.choice())
                .part(requestDto.part())
                .schedule(schedule)
                .build();

        return enrollmentRepository.save(enrollment);
    }

    private void validateEnrollmentAvailable(ScheduleType scheduleType) {
        if (!scheduleType.isEnrollmentAvailable()) {
            throw new IllegalStateException(ENROLLMENT_NOT_AVAILABLE.getMessage());
        }
    }

    private void validateChoice(
            Choice choice,
            User user,
            ProjectSchedule schedule
    ) {
        if (!user.isChoiceAvailable(schedule, choice)) {
            throw new IllegalArgumentException(CHOICE_NOT_AVAILABLE.getMessage());
        }
    }

    private void validateApplicantCanEnroll(
            User applicant,
            TeamBuildingProject project
    ) {
        if (applicant.hasRegisteredIdeaIn(project)) {
            throw new IllegalStateException(IDEA_CREATOR_CANNOT_ENROLL.getMessage());
        }
    }

    private void validateEnrollmentUnique(
            User applicant,
            Idea idea
    ) {
        boolean alreadyEnrolled = applicant.getEnrollments().stream()
                .anyMatch(enrollment -> enrollment.getIdea().equals(idea));

        if (alreadyEnrolled) {
            throw new IllegalArgumentException(ALREADY_ENROLL.getMessage());
        }
    }

    private void validateIdeaOwnership(User user, Idea idea) {
        if (!idea.getCreator().equals(user)) {
            throw new IllegalStateException(NOT_CREATOR_OF_IDEA.getMessage());
        }
    }

    private void validateInSchedule(IdeaEnrollment enrollment, ProjectSchedule currentSchedule) {
        ProjectSchedule enrollmentSchedule = enrollment.getSchedule();

        if (!enrollmentSchedule.equals(currentSchedule)) {
            throw new IllegalStateException(SCHEDULE_PASSED.getMessage());
        }
    }
}
