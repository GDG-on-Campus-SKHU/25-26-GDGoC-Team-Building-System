package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.ProjectScheduleResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.TeamBuildingInfoResponseDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
    private static final List<ProjectSchedule> DEFAULT_SCHEDULES = List.of();
    private static final List<ProjectScheduleResponseDto> EXPECTED_SCHEDULES_DTO = List.of();

    @InjectMocks
    private TeamBuildingInfoMapper teamBuildingInfoMapper;

    @Mock
    private ProjectScheduleMapper scheduleMapper;

    @BeforeEach
    void mock() {
        when(scheduleMapper.map(DEFAULT_SCHEDULES))
                .thenReturn(EXPECTED_SCHEDULES_DTO);
    }

    @Test
    void 아이디어를_제외한_엔티티의_정보를_DTO로_매핑한다() {
        // given
        TeamBuildingProject project = TeamBuildingProject.builder()
                .name(DEFAULT_NAME)
                .maxMemberCount(DEFAULT_MAX_MEMBER_COUNT)
                .schedules(DEFAULT_SCHEDULES)
                .build();
        setId(project, DEFAULT_ID);

        // when
        TeamBuildingInfoResponseDto dto = teamBuildingInfoMapper.map(project);

        // then
        assertThat(dto.projectId()).isEqualTo(project.getId());
        assertThat(dto.projectName()).isEqualTo(project.getName());
        assertThat(dto.maxMemberCount()).isEqualTo(project.getMaxMemberCount());
        assertThat(dto.schedules()).isEqualTo(EXPECTED_SCHEDULES_DTO);
    }

    private void setId(TeamBuildingProject project, long id) {
        ReflectionTestUtils.setField(project, "id", id);
    }
}
