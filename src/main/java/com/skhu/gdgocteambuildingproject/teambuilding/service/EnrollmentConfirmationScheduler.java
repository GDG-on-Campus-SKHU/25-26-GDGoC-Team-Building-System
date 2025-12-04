package com.skhu.gdgocteambuildingproject.teambuilding.service;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaStatus;
import com.skhu.gdgocteambuildingproject.Idea.repository.IdeaRepository;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.ProjectScheduleRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentConfirmationScheduler {

    private final ProjectScheduleRepository scheduleRepository;
    private final IdeaRepository ideaRepository;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void confirm() {
        List<ProjectSchedule> schedules = findShouldConfirmSchedules();

        for (ProjectSchedule schedule : schedules) {
            try {
                confirmEnrollmentsOf(schedule);
                schedule.markAsConfirm();
            } catch (Exception e) {
                log.error("일정(ID: {})에 대한 지원 처리 중 오류 발생", schedule.getId(), e);
                throw new RuntimeException(ExceptionMessage.SCHEDULE_CONFIRM_FAILED.getMessage());
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
        List<Idea> registeredIdeas = ideaRepository.findByScheduleIdAndRegisterStatus(
                schedule.getId(),
                IdeaStatus.REGISTERED
        );

        for (Idea idea : registeredIdeas) {
            idea.confirmEnrollments(schedule);
        }
    }
}

