package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProjectFilterTest {
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime DEFAULT_PAST_START_DATE = NOW.minusDays(10);
    private static final LocalDateTime DEFAULT_PAST_END_DATE = NOW.minusDays(1);
    private static final LocalDateTime DEFAULT_FUTURE_START_DATE = NOW.plusDays(1);
    private static final LocalDateTime DEFAULT_FUTURE_END_DATE = NOW.plusDays(10);

    private final ProjectFilter projectFilter = new ProjectFilter();

    @Nested
    class 완료되지_않은_프로젝트만_남긴다 {
        @Test
        void 종료일이_현재보다_미래인_프로젝트들을_반환한다() {
            // given
            TeamBuildingProject finishedProject = createFinishedProject();
            TeamBuildingProject upcomingProject = createUpcomingProject();
            List<TeamBuildingProject> projects = List.of(finishedProject, upcomingProject);

            // when
            List<TeamBuildingProject> filteredProjects = projectFilter.filterUnfinishedProjects(projects);

            // then
            assertThat(filteredProjects).containsExactly(upcomingProject);
        }
    }

    @Nested
    class 가장_일찍_시작할_프로젝트를_찾는다 {
        @Test
        void 시작일이_가장_이전인_프로젝트를_반환한다() {
            // given
            ProjectSchedule earliestSchedule = ProjectSchedule.builder()
                    .startDate(NOW.minusDays(10))
                    .endDate(NOW.minusDays(5))
                    .build();
            TeamBuildingProject earliestProject = TeamBuildingProject.builder()
                    .schedules(List.of(earliestSchedule))
                    .build();
            ProjectSchedule laterSchedule = ProjectSchedule.builder()
                    .startDate(NOW.plusDays(10))
                    .endDate(NOW.plusDays(15))
                    .build();
            TeamBuildingProject laterProject = TeamBuildingProject.builder()
                    .schedules(List.of(laterSchedule))
                    .build();
            List<TeamBuildingProject> projects = List.of(earliestProject, laterProject);

            // when
            Optional<TeamBuildingProject> result = projectFilter.findEarliestProject(projects);

            // then
            assertThat(result).isPresent();
            TeamBuildingProject resultProject = result.get();
            assertThat(resultProject).isSameAs(earliestProject);
        }
    }

    private TeamBuildingProject createFinishedProject() {
        ProjectSchedule pastSchedule = ProjectSchedule.builder()
                .startDate(DEFAULT_PAST_START_DATE)
                .endDate(DEFAULT_PAST_END_DATE)
                .build();

        return TeamBuildingProject.builder()
                .schedules(List.of(pastSchedule))
                .build();
    }

    private TeamBuildingProject createUpcomingProject() {
        ProjectSchedule pastSchedule = ProjectSchedule.builder()
                .startDate(DEFAULT_FUTURE_START_DATE)
                .endDate(DEFAULT_FUTURE_END_DATE)
                .build();

        return TeamBuildingProject.builder()
                .schedules(List.of(pastSchedule))
                .build();
    }
}
