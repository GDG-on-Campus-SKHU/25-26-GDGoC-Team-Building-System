package com.skhu.gdgocteambuildingproject.teambuilding.model;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.PartAvailabilityResponseDto;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PartAvailabilityMapper {
    public List<PartAvailabilityResponseDto> map(Idea idea) {
        return Arrays.stream(Part.values())
                .map(part -> getDtoOf(part, idea))
                .toList();
    }

    private PartAvailabilityResponseDto getDtoOf(
            Part part,
            Idea idea
    ) {
        return PartAvailabilityResponseDto.builder()
                .part(part)
                .available(idea.isEnrollmentAvailable(part))
                .build();
    }
}
