package com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.Choice;
import jakarta.validation.constraints.NotNull;

public record EnrollmentRequestDto(
        @NotNull
        Choice choice,
        @NotNull
        Part part
) {
}
