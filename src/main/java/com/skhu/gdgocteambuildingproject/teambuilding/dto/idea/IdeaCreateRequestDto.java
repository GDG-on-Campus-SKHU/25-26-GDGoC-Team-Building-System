package com.skhu.gdgocteambuildingproject.teambuilding.dto.idea;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.IdeaStatus;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record IdeaCreateRequestDto(
        String title,
        String introduction,
        String description,
        Long topicId,
        Part creatorPart,
        @NotNull(message = "등록 상태는 필수입니다.")
        IdeaStatus registerStatus,
        @Valid
        List<IdeaMemberCompositionRequestDto> compositions
) {
    @JsonIgnore
    public List<String> getTexts() {
        return List.of(title, introduction, description);
    }
}
