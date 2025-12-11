package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaMemberCompositionResponseDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class IdeaMemberCompositionMapper {

    public List<IdeaMemberCompositionResponseDto> map(Idea idea) {
        Map<Part, Integer> maxCounts = idea.getMaxMemberCountsByPart();
        Map<Part, Integer> currentCounts = idea.getCurrentMemberCountsByPart();

        List<IdeaMemberCompositionResponseDto> dtos = new ArrayList<>();
        for (Part part : idea.getAvailableParts()) {
            dtos.add(IdeaMemberCompositionResponseDto.builder()
                    .part(part)
                    .maxCount(maxCounts.getOrDefault(part, 0))
                    .currentCount(currentCounts.getOrDefault(part, 0))
                    .build()
            );
        }

        return Collections.unmodifiableList(dtos);
    }
}
