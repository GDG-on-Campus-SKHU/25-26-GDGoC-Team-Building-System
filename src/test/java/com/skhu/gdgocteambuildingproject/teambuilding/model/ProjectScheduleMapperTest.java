package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.ProjectScheduleResponseDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ProjectScheduleMapperTest {
    private static final String DEFAULT_NAME = "name";
    private static final LocalDateTime DEFAULT_START_DATE = LocalDateTime.now().minusDays(10);
    private static final LocalDateTime DEFAULT_END_DATE = LocalDateTime.now().plusDays(10);

    private final ProjectScheduleMapper projectScheduleMapper = new ProjectScheduleMapper();

    @Test
    void 엔티티의_정보를_DTO로_매핑한다() {
        // given
        ProjectSchedule schedule = ProjectSchedule.builder()
                .name(DEFAULT_NAME)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE)
                .build();

        // when
        ProjectScheduleResponseDto dto = projectScheduleMapper.map(schedule);

        // then
        assertThat(dto.scheduleName()).isEqualTo(DEFAULT_NAME);
        assertThat(dto.startAt()).isEqualTo(DEFAULT_START_DATE);
        assertThat(dto.endAt()).isEqualTo(DEFAULT_END_DATE);
    }
}
