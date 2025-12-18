package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.TeamBuildingProjectRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProjectUtilTest {
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime AFTER_THAN_NOW = NOW.plusDays(100);
    private static final LocalDateTime FUTURE_DATE = NOW.plusDays(1000);

    @Mock
    private TeamBuildingProjectRepository projectRepository;

    @InjectMocks
    private ProjectUtil projectUtil;

    @Test
    void 시작일이_가장_빠른_프로젝트를_반환한다() {
        // given
        TeamBuildingProject earlierProject = mock(TeamBuildingProject.class);
        TeamBuildingProject laterProject = mock(TeamBuildingProject.class);

        setAsScheduled(earlierProject);
        setAsScheduled(laterProject);
        setAsUnfinished(earlierProject);
        setAsUnfinished(laterProject);

        when(earlierProject.getStartDate()).thenReturn(NOW);
        when(laterProject.getStartDate()).thenReturn(AFTER_THAN_NOW);

        when(projectRepository.findProjectsWithScheduleNotStartedBefore(any(), any())).thenReturn(
                List.of(earlierProject, laterProject)
        );

        // when
        Optional<TeamBuildingProject> result = projectUtil.findCurrentProject();

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isSameAs(earlierProject);
    }

    @Test
    void 일정이_설정된_프로젝트가_없다면_일정이_설정되지_않은_프로젝트를_반환한다() {
        // given
        TeamBuildingProject unscheduledProject = mock(TeamBuildingProject.class);

        setAsUnscheduled(unscheduledProject);

        when(projectRepository.findProjectsWithScheduleNotStartedBefore(any(), any())).thenReturn(
                List.of(unscheduledProject)
        );

        // when
        Optional<TeamBuildingProject> result = projectUtil.findCurrentProject();

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isSameAs(unscheduledProject);
    }

    private void setAsScheduled(TeamBuildingProject mockProject) {
        when(mockProject.isUnscheduled()).thenReturn(false);
        when(mockProject.isScheduled()).thenReturn(true);
    }

    private void setAsUnscheduled(TeamBuildingProject mockProject) {
        when(mockProject.isUnscheduled()).thenReturn(true);
        when(mockProject.isScheduled()).thenReturn(false);
    }

    private void setAsUnfinished(TeamBuildingProject mockProject) {
        when(mockProject.getEndDate()).thenReturn(FUTURE_DATE);
    }
}
