package com.skhu.gdgocteambuildingproject.teambuilding.dto.idea;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record IdeaTextUpdateRequestDto(
        @NotBlank(message = "제목은 필수입니다.")
        String title,
        @NotBlank(message = "한줄 소개는 필수입니다.")
        String introduction,
        @NotBlank(message = "설명은 필수입니다.")
        String description,
        Long topicId
) {
    @JsonIgnore
    public List<String> getTexts() {
        return List.of(title, introduction, description);
    }
}
