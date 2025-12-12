package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.activity.ActivityResponseDto;
import com.skhu.gdgocteambuildingproject.activity.domain.Activity;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {
    public ActivityResponseDto toActivityResponseDto(Activity activity) {
        return ActivityResponseDto.builder()
                .categoryId(activity.getActivityCategory().getId())
                .categoryTitle(activity.getActivityCategory().getName())
                .postTitle(activity.getTitle())
                .postId(activity.getId())
                .generation(activity.getGeneration())
                .speaker(activity.getSpeaker())
                .videoUrl(activity.getVideoUrl())
                .thumbnailUrl(activity.getThumbnailUrl())
                .build();
    }
}
