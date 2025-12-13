package com.skhu.gdgocteambuildingproject.teambuilding.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ALREADY_ENROLL;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.CHOICE_NOT_AVAILABLE;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ENROLLMENT_BY_OTHER_USER;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ENROLLMENT_NOT_AVAILABLE;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ENROLLMENT_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_MEMBER_CANNOT_ENROLL;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.NOT_CREATOR_OF_IDEA;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.REGISTERED_IDEA_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_PASSED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.USER_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.Choice;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.IdeaStatus;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentDetermineRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.ReceivedEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.SentEnrollmentsResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ParticipationUtil;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ProjectUtil;
import com.skhu.gdgocteambuildingproject.teambuilding.model.mapper.EnrollmentAvailabilityMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.mapper.ReceivedEnrollmentMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.mapper.SentEnrollmentMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.IdeaEnrollmentRepository;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.IdeaRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;
    private final IdeaEnrollmentRepository enrollmentRepository;

    private final EnrollmentAvailabilityMapper availabilityMapper;
    private final SentEnrollmentMapper sentEnrollmentMapper;
    private final ReceivedEnrollmentMapper receivedEnrollmentMapper;
    private final ProjectUtil projectUtil;
    private final ParticipationUtil participationUtil;

    @Override
    @Transactional
    public void enroll(
            long applicantId,
            long ideaId,
            EnrollmentRequestDto requestDto
    ) {
        TeamBuildingProject currentProject = findCurrentProject();
        participationUtil.validateParticipation(applicantId, currentProject.getId());

        User applicant = findUserBy(applicantId);
        Idea idea = findIdeaBy(ideaId);

        ProjectSchedule currentSchedule = findCurrentScheduleOf(currentProject);

        validateEnrollmentAvailable(currentSchedule.getType());
        validateChoice(requestDto.choice(), applicant, currentSchedule);
        validateApplicantCanEnroll(applicant, currentProject);
        validateEnrollmentUnique(applicant, idea, currentSchedule);

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
        TeamBuildingProject currentProject = findCurrentProject();
        participationUtil.validateParticipation(userId, currentProject.getId());

        User creator = findUserBy(userId);
        IdeaEnrollment enrollment = findEnrollmentWithLock(enrollmentId);
        Idea idea = enrollment.getIdea();

        ProjectSchedule currentSchedule = findCurrentScheduleOf(currentProject);

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
    public EnrollmentAvailabilityResponseDto getAvailabilityInfo(
            long ideaId,
            long applicantId
    ) {
        Idea idea = findIdeaBy(ideaId);
        TeamBuildingProject project = idea.getProject();
        participationUtil.validateParticipation(applicantId, project.getId());

        ProjectSchedule currentSchedule = findCurrentScheduleOf(project);
        User applicant = findUserBy(applicantId);

        validateEnrollmentAvailable(currentSchedule.getType());

        return availabilityMapper.map(project, currentSchedule, idea, applicant);
    }

    @Override
    @Transactional(readOnly = true)
    public SentEnrollmentsResponseDto getSentEnrollments(
            long userId,
            ScheduleType scheduleType
    ) {
        validateEnrollmentAvailable(scheduleType);

        User user = findUserBy(userId);
        TeamBuildingProject currentProject = findCurrentProject();
        participationUtil.validateParticipation(userId, currentProject.getId());

        ProjectSchedule schedule = currentProject.getScheduleFrom(scheduleType);

        List<IdeaEnrollment> enrollments = user.getEnrollmentFrom(schedule);

        return sentEnrollmentMapper.map(enrollments, schedule);
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
        participationUtil.validateParticipation(userId, currentProject.getId());

        Idea idea = findRegisteredIdeaOf(user, currentProject);

        ProjectSchedule schedule = currentProject.getScheduleFrom(scheduleType);
        List<IdeaEnrollment> enrollments = idea.getEnrollmentsOf(schedule);

        return enrollments.stream()
                .map(enrollment -> receivedEnrollmentMapper.map(enrollment, idea))
                .toList();
    }

    @Override
    @Transactional
    public void cancelEnrollment(long userId, long enrollmentId) {
        User user = findUserBy(userId);
        IdeaEnrollment enrollment = findEnrollmentBy(enrollmentId);
        Idea idea = enrollment.getIdea();
        TeamBuildingProject project = idea.getProject();

        participationUtil.validateParticipation(userId, project.getId());

        validateEnrollmentOwnership(enrollment, user);
        validateEnrollmentCancelable(enrollment);

        removeEnrollment(enrollment);
    }

    private Idea findIdeaBy(long ideaId) {
        return ideaRepository.findById(ideaId)
                .orElseThrow(() -> new EntityNotFoundException(IDEA_NOT_EXIST.getMessage()));
    }

    private IdeaEnrollment findEnrollmentBy(long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException(ENROLLMENT_NOT_EXIST.getMessage()));
    }

    private IdeaEnrollment findEnrollmentWithLock(long enrollmentId) {
        return enrollmentRepository.findByIdWithIdeaLock(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException(ENROLLMENT_NOT_EXIST.getMessage()));
    }

    private ProjectSchedule findCurrentScheduleOf(TeamBuildingProject project) {
        return project.getCurrentSchedule()
                .orElseThrow(() -> new IllegalStateException(ENROLLMENT_NOT_AVAILABLE.getMessage()));
    }

    private User findUserBy(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXIST.getMessage()));
    }

    private TeamBuildingProject findCurrentProject() {
        return projectUtil.findCurrentProject()
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
        if (applicant.isMemberOf(project)) {
            throw new IllegalStateException(IDEA_MEMBER_CANNOT_ENROLL.getMessage());
        }
    }

    private void validateEnrollmentOwnership(IdeaEnrollment enrollment, User user) {
        User applicant = enrollment.getApplicant();

        if (!applicant.equals(user)) {
            throw new IllegalArgumentException(ENROLLMENT_BY_OTHER_USER.getMessage());
        }
    }

    private void validateEnrollmentCancelable(IdeaEnrollment enrollment) {
        if (!enrollment.isCancelable()) {
            throw new IllegalStateException(ExceptionMessage.ENROLLMENT_NOT_CANCELABLE.getMessage());
        }
    }

    private void validateEnrollmentUnique(
            User applicant,
            Idea idea,
            ProjectSchedule currentSchedule
    ) {
        boolean alreadyEnrolled = applicant.getEnrollments().stream()
                .filter(enrollment -> currentSchedule.equals(enrollment.getSchedule()))
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

    private void removeEnrollment(IdeaEnrollment enrollment) {
        User applicant = enrollment.getApplicant();
        Idea idea = enrollment.getIdea();

        applicant.removeEnrollment(enrollment);
        idea.removeEnrollment(enrollment);
    }
}
