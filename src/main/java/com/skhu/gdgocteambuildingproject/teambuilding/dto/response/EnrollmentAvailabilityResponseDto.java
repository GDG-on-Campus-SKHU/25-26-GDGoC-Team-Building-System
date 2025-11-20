package com.skhu.gdgocteambuildingproject.teambuilding.dto.response;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import java.util.List;
import lombok.Builder;

@Builder
public record EnrollmentAvailabilityResponseDto(
        String projectName,
        ScheduleType scheduleType,

        String ideaTitle,
        String ideaIntroduction,

        String creatorSchool,
        Part creatorPart,
        String creatorName,

        List<ChoiceAvailabilityResponseDto> choiceAvailabilities,
        List<PartAvailabilityResponseDto> partAvailabilities
) {
}
