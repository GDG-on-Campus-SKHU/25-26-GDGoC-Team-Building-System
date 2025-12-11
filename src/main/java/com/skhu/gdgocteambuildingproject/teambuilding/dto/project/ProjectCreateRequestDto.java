package com.skhu.gdgocteambuildingproject.teambuilding.dto.project;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProjectCreateRequestDto(
        @NotBlank(message = "프로젝트 이름은 비워둘 수 없습니다.")
        String projectName,

        @Min(value = 1, message = "최대 인원은 1명 이상이어야 합니다.")
        int maxMemberCount
) {
}
