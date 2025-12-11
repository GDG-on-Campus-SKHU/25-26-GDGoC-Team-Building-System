package com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.Choice;

public record EnrollmentRequestDto(
        Choice choice,
        Part part
) {
}
