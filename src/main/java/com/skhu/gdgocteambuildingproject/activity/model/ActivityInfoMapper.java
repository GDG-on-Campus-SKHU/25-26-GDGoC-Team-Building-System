package com.skhu.gdgocteambuildingproject.activity.model;

import com.skhu.gdgocteambuildingproject.activity.domain.Activity;
import com.skhu.gdgocteambuildingproject.activity.domain.ActivityCategory;
import com.skhu.gdgocteambuildingproject.activity.dto.ActivityInfoResponseDto;
import com.skhu.gdgocteambuildingproject.activity.dto.ActivitySummaryResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActivityInfoMapper {

    public ActivitySummaryResponseDto mapToSummary(
            ActivityCategory activityCategory,
            List<ActivityInfoResponseDto> activities
    ) {
        return ActivitySummaryResponseDto.builder()
                .categoryId(activityCategory.getId())
                .categoryTitle(activityCategory.getName())
                .activities(activities)
                .build();
    }

    public ActivityInfoResponseDto mapToInfo(Activity activity) {
        return ActivityInfoResponseDto.builder()
                .title(activity.getTitle())
                .speaker(activity.getSpeaker())
                .generation(activity.getGeneration().getLabel())
                .videoUrl(activity.getVideoUrl())
                .thumbnailUrl(activity.getThumbnailUrl())
                .build();
    }

    public List<ActivityInfoResponseDto> mapToInfoList(List<Activity> activities) {
        return activities.stream()
                .map(this::mapToInfo)
                .toList();
    }
}
