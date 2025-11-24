package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectUtilTest {
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime AFTER_THAN_NOW = NOW.plusDays(100);
    private static final LocalDateTime PAST_DATE = NOW.minusDays(1000);
    private static final LocalDateTime FUTURE_DATE = NOW.plusDays(1000);

    private final ProjectUtil projectUtil = new ProjectUtil();

    @Test
    void 시작일이_가장_빠른_프로젝트를_반환한다() {
        // given
        TeamBuildingProject earlierProject = mock(TeamBuildingProject.class);
        TeamBuildingProject laterProject = mock(TeamBuildingProject.class);
        List<TeamBuildingProject> projects = List.of(earlierProject, laterProject);

        setAsScheduled(earlierProject);
        setAsScheduled(laterProject);
        setAsUnfinished(earlierProject);
        setAsUnfinished(laterProject);

        when(earlierProject.getStartDate()).thenReturn(NOW);
        when(laterProject.getStartDate()).thenReturn(AFTER_THAN_NOW);

        // when
        Optional<TeamBuildingProject> result = projectUtil.findCurrentProject(projects);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isSameAs(earlierProject);
    }

    @Test
    void 이미_종료된_프로젝트는_제외한다() {
        // given: 종료일이 현재보다 과거인 프로젝트
        TeamBuildingProject finishedProject = mock(TeamBuildingProject.class);

        when(finishedProject.getEndDate()).thenReturn(PAST_DATE);

        // when
        Optional<TeamBuildingProject> result = projectUtil.findCurrentProject(List.of(finishedProject));

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void 일정이_설정된_프로젝트가_없다면_일정이_설정되지_않은_프로젝트를_반환한다() {
        // given
        TeamBuildingProject unscheduledProject = mock(TeamBuildingProject.class);

        setAsUnscheduled(unscheduledProject);

        // when
        Optional<TeamBuildingProject> result = projectUtil.findCurrentProject(List.of(unscheduledProject));

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
