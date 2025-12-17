package com.skhu.gdgocteambuildingproject.teambuilding.dto.project;

import jakarta.validation.constraints.NotBlank;

public record ProjectCreateRequestDto(
        @NotBlank(message = "프로젝트 이름은 비워둘 수 없습니다.")
        String projectName
) {
}
