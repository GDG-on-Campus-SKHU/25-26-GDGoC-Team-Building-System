package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.activity.domain.Activity;
import com.skhu.gdgocteambuildingproject.admin.dto.activity.PostResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {
    public PostResponseDto toPostResponseDto(Activity activity) {
        return PostResponseDto.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .speaker(activity.getSpeaker())
                .generation(activity.getGeneration().getLabel())
                .videoUrl(activity.getVideoUrl())
                .build();
    }
}
