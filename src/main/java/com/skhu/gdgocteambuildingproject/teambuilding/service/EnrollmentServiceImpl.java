package com.skhu.gdgocteambuildingproject.teambuilding.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ENROLLMENT_NOT_AVAILABLE;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ENROLLMENT_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.NOT_CREATOR_OF_IDEA;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.SCHEDULE_PASSED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.USER_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.Idea.repository.IdeaRepository;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.EnrollmentDetermineRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.ApplicantEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.EnrollmentAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ApplicantEnrollmentMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.EnrollmentAvailabilityMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.ProjectUtil;
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
    private final ApplicantEnrollmentMapper applicantEnrollmentMapper;
    private final ProjectUtil projectUtil;

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
    public List<ApplicantEnrollmentResponseDto> getApplyHistory(long userId, ScheduleType scheduleType) {
        validateEnrollmentAvailable(scheduleType);

        User user = findUserBy(userId);
        TeamBuildingProject currentProject = findCurrentProject();
        ProjectSchedule schedule = currentProject.getScheduleFrom(scheduleType);

        List<IdeaEnrollment> enrollments = user.getEnrollmentFrom(schedule);

        return enrollments.stream()
                .map(enrollment -> applicantEnrollmentMapper.map(enrollment, schedule))
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

    private void validateEnrollmentAvailable(ScheduleType scheduleType) {
        if (!scheduleType.isEnrollmentAvailable()) {
            throw new IllegalStateException(ENROLLMENT_NOT_AVAILABLE.getMessage());
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
