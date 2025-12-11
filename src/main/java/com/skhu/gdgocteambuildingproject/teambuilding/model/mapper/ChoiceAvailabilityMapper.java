package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.Choice;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.ChoiceAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChoiceAvailabilityMapper {
    public List<ChoiceAvailabilityResponseDto> map(
            User applicant,
            ProjectSchedule schedule
    ) {
        return Arrays.stream(Choice.values())
                .map(choice -> createDtoOf(choice, applicant, schedule))
                .toList();
    }

    private ChoiceAvailabilityResponseDto createDtoOf(
            Choice choice,
            User applicant,
            ProjectSchedule schedule
    ) {
        return ChoiceAvailabilityResponseDto.builder()
                .choice(choice)
                .available(applicant.isChoiceAvailable(schedule, choice))
                .build();
    }
}
