package com.skhu.gdgocteambuildingproject.teambuilding.dto.idea;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record IdeaUpdateRequestDto(
        @NotBlank(message = "제목은 필수입니다.")
        String title,
        @NotBlank(message = "한줄 소개는 필수입니다.")
        String introduction,
        @NotBlank(message = "설명은 필수입니다.")
        String description,
        @NotNull(message = "주제는 필수입니다.")
        Long topicId,
        @NotNull(message = "생성자 파트는 필수입니다.")
        Part creatorPart,
        @Valid
        List<IdeaMemberCompositionRequestDto> compositions
) {
    @JsonIgnore
    public List<String> getTexts() {
        return List.of(title, introduction, description);
    }
}
