package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectFilterTest {
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime PAST_DATE = NOW.minusDays(100);
    private static final LocalDateTime FUTURE_DATE = NOW.plusDays(100);

    private final ProjectFilter projectFilter = new ProjectFilter();

    @Nested
    class 완료되지_않은_프로젝트만_남긴다 {
        @Test
        void 종료일이_현재보다_미래인_프로젝트들을_반환한다() {
            // given
            TeamBuildingProject finishedProject = mock(TeamBuildingProject.class);
            TeamBuildingProject upcomingProject = mock(TeamBuildingProject.class);
            List<TeamBuildingProject> projects = List.of(finishedProject, upcomingProject);

            when(finishedProject.isUnscheduled()).thenReturn(false);
            when(upcomingProject.isUnscheduled()).thenReturn(false);
            when(finishedProject.getEndDate()).thenReturn(PAST_DATE);
            when(upcomingProject.getEndDate()).thenReturn(FUTURE_DATE);

            // when
            List<TeamBuildingProject> filteredProjects = projectFilter.filterUnfinishedProjects(projects);

            // then
            assertThat(filteredProjects).containsExactly(upcomingProject);
        }
    }

    @Nested
    class 일정이_정해지지_않은_프로젝트를_반환한다 {
        @Test
        void 일정이_정해지지_않은_프로젝트들만_남긴다() {
            // given
            TeamBuildingProject scheduledProject = mock(TeamBuildingProject.class);
            TeamBuildingProject unscheduledProject = mock(TeamBuildingProject.class);
            List<TeamBuildingProject> projects = List.of(unscheduledProject, scheduledProject);

            lenient().when(scheduledProject.isUnscheduled()).thenReturn(false);
            lenient().when(unscheduledProject.isUnscheduled()).thenReturn(true);

            // when
            Optional<TeamBuildingProject> result = projectFilter.findUnscheduledProject(projects);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(unscheduledProject);
        }
    }

    @Nested
    class 가장_일찍_시작할_프로젝트를_찾는다 {
        @Test
        void 아이디어_등록_일정의_시작일이_가장_이전인_프로젝트를_반환한다() {
            // given
            LocalDateTime earlierTime = NOW.plusHours(1);
            LocalDateTime laterTime = NOW.plusHours(10);

            TeamBuildingProject earliestProject = mock(TeamBuildingProject.class);
            TeamBuildingProject laterProject = mock(TeamBuildingProject.class);
            List<TeamBuildingProject> projects = List.of(earliestProject, laterProject);

            when(earliestProject.isScheduled()).thenReturn(true);
            when(laterProject.isScheduled()).thenReturn(true);
            when(earliestProject.getStartDate()).thenReturn(earlierTime);
            when(laterProject.getStartDate()).thenReturn(laterTime);

            // when
            Optional<TeamBuildingProject> result = projectFilter.findEarliestScheduledProject(projects);

            // then
            assertThat(result).isPresent();
            TeamBuildingProject resultProject = result.get();
            assertThat(resultProject).isSameAs(earliestProject);
        }
    }
}
