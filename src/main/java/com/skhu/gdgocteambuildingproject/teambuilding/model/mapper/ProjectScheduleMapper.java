package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectScheduleResponseDto;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectScheduleMapper {
    public List<ProjectScheduleResponseDto> map(List<ProjectSchedule> schedules) {
        return schedules.stream()
                .map(this::map)
                .toList();
    }

    public ProjectScheduleResponseDto map(ProjectSchedule schedule) {
        return ProjectScheduleResponseDto.builder()
                .scheduleType(schedule.getType())
                .startAt(schedule.getStartDate())
                .endAt(schedule.getEndDate())
                .build();
    }
}
