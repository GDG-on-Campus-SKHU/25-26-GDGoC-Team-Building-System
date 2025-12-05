package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaEnrollment;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.Choice;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.IdeaEnrollmentRepository;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.ProjectScheduleRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentConfirmationScheduler {

    private static final long CONFIRMATION_INTERVAL_MS = 60_000; // 1분

    private final ProjectScheduleRepository scheduleRepository;
    private final IdeaEnrollmentRepository enrollmentRepository;

    @Scheduled(fixedDelay = CONFIRMATION_INTERVAL_MS)
    @Transactional
    public void confirm() {
        List<ProjectSchedule> schedules = findShouldConfirmSchedules();

        for (ProjectSchedule schedule : schedules) {
            try {
                confirmEnrollmentsOf(schedule);
                schedule.markAsConfirm();
            } catch (Exception e) {
                log.error("일정(ID: {})에 대한 지원 처리 중 오류 발생", schedule.getId(), e);
                throw new RuntimeException(ExceptionMessage.SCHEDULE_CONFIRM_FAILED.getMessage(), e);
            }
        }
    }

    private List<ProjectSchedule> findShouldConfirmSchedules() {
        LocalDateTime now = LocalDateTime.now();
        List<ProjectSchedule> unconfirmedSchedules = scheduleRepository.findUnconfirmedSchedulesEndedBefore(now);

        return unconfirmedSchedules.stream()
                .filter(ProjectSchedule::isScheduled)
                .toList();
    }

    private void confirmEnrollmentsOf(ProjectSchedule schedule) {
        List<IdeaEnrollment> enrollments = enrollmentRepository.findBySchedule(schedule);
        Map<User, List<IdeaEnrollment>> enrollmentsGroupedByApplicant = groupByApplicant(enrollments);

        List<IdeaEnrollment> acceptableEnrollments = extractAcceptableEnrollments(enrollmentsGroupedByApplicant);
        acceptEnrollmentsAsMember(acceptableEnrollments);

        enrollments.forEach(IdeaEnrollment::confirmStatus);
    }

    private List<IdeaEnrollment> extractAcceptableEnrollments(Map<User, List<IdeaEnrollment>> enrollmentsGroupedByApplicant) {
        return enrollmentsGroupedByApplicant.values().stream()
                .map(this::extractHighestPriorityAcceptableEnrollment)
                .flatMap(Optional::stream)
                .toList();
    }

    private Map<User, List<IdeaEnrollment>> groupByApplicant(List<IdeaEnrollment> enrollments) {
        return enrollments.stream()
                .collect(Collectors.groupingBy(IdeaEnrollment::getApplicant));
    }

    private void acceptEnrollmentsAsMember(List<IdeaEnrollment> acceptableEnrollments) {
        for (IdeaEnrollment enrollment : acceptableEnrollments) {
            Idea idea = enrollment.getIdea();
            idea.acceptAsMember(enrollment);
        }
    }

    private Optional<IdeaEnrollment> extractHighestPriorityAcceptableEnrollment(List<IdeaEnrollment> enrollments) {
        return enrollments.stream()
                .filter(IdeaEnrollment::isScheduledToAccept)
                .min(Comparator.comparing(IdeaEnrollment::getChoice, Choice.comparator()));
    }
}

