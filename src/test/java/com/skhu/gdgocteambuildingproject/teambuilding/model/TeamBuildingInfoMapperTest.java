package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectTotalResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.TeamBuildingInfoResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import java.util.List;
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

    @Nested
    class 엔티티_정보_매핑 {
        @Test
        void 아이디어를_제외한_엔티티의_정보를_DTO로_매핑한다() {
            // given
            project = TeamBuildingProject.builder()
                    .name(DEFAULT_NAME)
                    .maxMemberCount(DEFAULT_MAX_MEMBER_COUNT)
                    .build();
            setId(project, DEFAULT_ID);

            ProjectTotalResponseDto projectTotal = ProjectTotalResponseDto.builder()
                    .projectId(DEFAULT_ID)
                    .projectName(DEFAULT_NAME)
                    .maxMemberCount(DEFAULT_MAX_MEMBER_COUNT)
                    .availableParts(List.of())
                    .schedules(List.of())
                    .build();

            when(projectTotalMapper.map(project))
                    .thenReturn(projectTotal);

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project, user);

            // then
            assertThat(dto.project()).isNotNull();
            assertThat(dto.project().projectId()).isEqualTo(project.getId());
            assertThat(dto.project().projectName()).isEqualTo(project.getName());
            assertThat(dto.project().maxMemberCount()).isEqualTo(project.getMaxMemberCount());
        }
    }

    @Nested
    class 아이디어_등록_가능_여부_반환 {
        @Test
        void 등록된_아이디어가_있다면_false를_반환한다() {
            // given: 해당 프로젝트에 등록된 아이디어가 존재하는 상황
            when(user.getIdeas()).thenReturn(List.of(idea));
            when(idea.getProject()).thenReturn(project);
            when(idea.isRegistered()).thenReturn(true);

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project, user);

            // then
            assertThat(dto.registrable()).isFalse();
        }

        @Test
        void 등록된_아이디어가_없다면_true를_반환한다() {
            // given: 해당 프로젝트에 등록된 아이디어가 존재하지 않는 상황
            when(user.getIdeas()).thenReturn(List.of());

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project, user);

            // then
            assertThat(dto.registrable()).isTrue();
        }

        @Test
        void 임시저장된_아이디어만_있다면_true를_반환한다() {
            // given: 해당 프로젝트에 임시저장된 아이디어가 존재하는 상황
            when(user.getIdeas()).thenReturn(List.of(idea));
            when(idea.getProject()).thenReturn(project);
            when(idea.isRegistered()).thenReturn(false);

            // when
            TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project, user);

            // then
            assertThat(dto.registrable()).isTrue();
        }
    }

    private void setId(TeamBuildingProject project, long id) {
        ReflectionTestUtils.setField(project, "id", id);
    }
}
