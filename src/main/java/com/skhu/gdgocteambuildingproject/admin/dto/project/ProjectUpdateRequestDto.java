package com.skhu.gdgocteambuildingproject.admin.dto.project;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ProjectUpdateRequestDto(
        @NotBlank(message = "프로젝트 이름은 비워둘 수 없습니다.")
        String projectName,

        @Min(value = 1, message = "최대 인원은 1명 이상이어야 합니다.")
        int maxMemberCount,

        List<Part> availableParts,

        List<String> topics,

        List<Long> participantUserIds,

        List<ScheduleUpdateRequestDto> schedules
) {
}

