package com.skhu.gdgocteambuildingproject.teambuilding.dto.project;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ProjectUpdateRequestDto(
        @Min(value = 1, message = "최대 인원은 1명 이상이어야 합니다.")
        int maxMemberCount,

        @NotNull
        List<Part> availableParts,

        @NotNull
        List<String> topics,

        @NotNull
        List<Long> participantUserIds,

        @NotNull
        List<ScheduleUpdateRequestDto> schedules
) {
}

