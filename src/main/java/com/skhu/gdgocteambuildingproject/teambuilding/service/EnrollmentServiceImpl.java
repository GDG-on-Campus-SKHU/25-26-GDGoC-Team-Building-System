package com.skhu.gdgocteambuildingproject.teambuilding.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.ENROLLMENT_NOT_AVAILABLE;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.IDEA_NOT_EXIST;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.USER_NOT_EXIST;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.repository.IdeaRepository;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.EnrollmentAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.model.EnrollmentAvailabilityMapper;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;

    private final EnrollmentAvailabilityMapper availabilityMapper;

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

        validateEnrollmentAvailable(currentSchedule);

        return availabilityMapper.map(project, currentSchedule, idea, applicant);
    }

    private Idea findIdeaBy(long ideaId) {
        return ideaRepository.findById(ideaId)
                .orElseThrow(() -> new EntityNotFoundException(IDEA_NOT_EXIST.getMessage()));
    }

    private ProjectSchedule findCurrentSchedule(TeamBuildingProject project) {
        return project.getCurrentSchedule()
                .orElseThrow(() -> new IllegalStateException(ENROLLMENT_NOT_AVAILABLE.getMessage()));
    }

    private User findUserBy(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST.getMessage()));
    }

    private void validateEnrollmentAvailable(ProjectSchedule schedule) {
        if (!schedule.isEnrollmentAvailable()) {
            throw new IllegalStateException(ENROLLMENT_NOT_AVAILABLE.getMessage());
        }
    }
}
