package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectTotalResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.TeamBuildingInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.model.mapper.ProjectTotalMapper;
import com.skhu.gdgocteambuildingproject.teambuilding.model.mapper.TeamBuildingInfoMapper;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TeamBuildingInfoMapperTest {
    private static final long DEFAULT_ID = 999L;
    private static final String DEFAULT_NAME = "name";
    private static final int DEFAULT_MAX_MEMBER_COUNT = 5;

    @InjectMocks
    private TeamBuildingInfoMapper teamBuildingInfoMapper;

    @Mock
    private ProjectTotalMapper projectTotalMapper;

    @Mock
    private User user;
    @Mock
    private Idea idea;
    @Mock
    private TeamBuildingProject project;
    @Mock
    private ProjectSchedule schedule;

    @Nested
    class 엔티티_정보_매핑 {
        @Test
        void 아이디어를_제외한_엔티티의_정보를_DTO로_매핑한다() {
            // given
            TeamBuildingProject actualProject = TeamBuildingProject.builder()
                    .name(DEFAULT_NAME)
                    .maxMemberCount(DEFAULT_MAX_MEMBER_COUNT)
                    .build();
            actualProject.initSchedules();
            setId(actualProject, DEFAULT_ID);

            ProjectTotalResponseDto projectTotal = ProjectTotalResponseDto.builder()
                    .projectId(DEFAULT_ID)
                    .projectName(DEFAULT_NAME)
                    .maxMemberCount(DEFAULT_MAX_MEMBER_COUNT)
                    .availableParts(List.of())
                    .schedules(List.of())
                    .build();

            when(projectTotalMapper.map(actualProject))
                    .thenReturn(projectTotal);

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(actualProject, user);

            // then
            assertThat(dto.project()).isNotNull();
            assertThat(dto.project().projectId()).isEqualTo(actualProject.getId());
            assertThat(dto.project().projectName()).isEqualTo(actualProject.getName());
            assertThat(dto.project().maxMemberCount()).isEqualTo(actualProject.getMaxMemberCount());
        }
    }

    @Nested
    class 아이디어_등록_가능_여부_반환 {
        @Test
        void 아이디어_등록_일정이고_등록된_아이디어가_없다면_true를_반환한다() {
            // given: 아이디어 등록 일정이고, 해당 프로젝트에 등록된 아이디어가 존재하지 않는 상황
            when(project.getCurrentSchedule()).thenReturn(Optional.of(schedule));
            when(schedule.isIdeaRegistrable()).thenReturn(true);
            when(user.getIdeas()).thenReturn(List.of());

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project, user);

            // then
            assertThat(dto.registrable()).isTrue();
        }

        @Test
        void 아이디어_등록_일정이고_임시저장된_아이디어만_있다면_true를_반환한다() {
            // given: 아이디어 등록 일정이고, 해당 프로젝트에 임시저장된 아이디어가 존재하는 상황
            when(project.getCurrentSchedule()).thenReturn(Optional.of(schedule));
            when(schedule.isIdeaRegistrable()).thenReturn(true);

            when(user.getIdeas()).thenReturn(List.of(idea));
            when(idea.getProject()).thenReturn(project);
            when(idea.isRegistered()).thenReturn(false);

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project, user);

            // then
            assertThat(dto.registrable()).isTrue();
        }

        @Test
        void 등록된_아이디어가_있다면_false를_반환한다() {
            // given: 해당 프로젝트에 등록된 아이디어가 존재하는 상황
            when(user.getIdeas()).thenReturn(List.of(idea));
            when(idea.getProject()).thenReturn(project);
            when(idea.isRegistered()).thenReturn(true);

            when(project.getCurrentSchedule()).thenReturn(Optional.of(schedule));
            when(schedule.isIdeaRegistrable()).thenReturn(true);

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project, user);

            // then
            assertThat(dto.registrable()).isFalse();
        }

        @Test
        void 아이디어_등록_일정이_아니면_false를_반환한다() {
            // given: 아이디어 등록 일정이 아닌 상황
            when(project.getCurrentSchedule()).thenReturn(Optional.of(schedule));
            when(schedule.isIdeaRegistrable()).thenReturn(false);

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project, user);

            // then
            assertThat(dto.registrable()).isFalse();
        }

        @Test
        void 현재_일정이_없으면_false를_반환한다() {
            // given: 현재 일정이 없는 상황
            when(project.getCurrentSchedule()).thenReturn(Optional.empty());

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project, user);

            // then
            assertThat(dto.registrable()).isFalse();
        }
    }

    @Nested
    class 지원_가능_여부_반환 {
        @Test
        void 지원_가능한_일정이고_팀에_소속되어_있지_않다면_true를_반환한다() {
            // given: 지원 가능한 일정이고, 사용자가 멤버가 아닌 상황
            when(project.getCurrentSchedule()).thenReturn(Optional.of(schedule));
            when(schedule.isEnrollmentAvailable()).thenReturn(true);
            when(user.isMemberOf(project)).thenReturn(false);

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project, user);

            // then
            assertThat(dto.canEnroll()).isTrue();
        }

        @Test
        void 팀에_소속되어_있다면_false를_반환한다() {
            // given: 사용자가 멤버인 상황
            when(user.isMemberOf(project)).thenReturn(true);

            when(project.getCurrentSchedule()).thenReturn(Optional.of(schedule));
            when(schedule.isEnrollmentAvailable()).thenReturn(true);

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project, user);

            // then
            assertThat(dto.canEnroll()).isFalse();
        }

        @Test
        void 지원_불가능한_일정이면_false를_반환한다() {
            // given: 지원 불가능한 일정인 상황
            when(project.getCurrentSchedule()).thenReturn(Optional.of(schedule));
            when(schedule.isEnrollmentAvailable()).thenReturn(false);

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project, user);

            // then
            assertThat(dto.canEnroll()).isFalse();
        }

        @Test
        void 현재_일정이_없으면_false를_반환한다() {
            // given: 현재 일정이 없는 상황
            when(project.getCurrentSchedule()).thenReturn(Optional.empty());

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project, user);

            // then
            assertThat(dto.canEnroll()).isFalse();
        }
    }

    private void setId(TeamBuildingProject project, long id) {
        ReflectionTestUtils.setField(project, "id", id);
    }
}
