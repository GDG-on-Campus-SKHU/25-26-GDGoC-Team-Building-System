package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.activity.ActivityCategoryInfoResponseDto;
import com.skhu.gdgocteambuildingproject.community.domain.ActivityCategory;
import org.springframework.stereotype.Component;

@Component
public class ActivityCategoryInfoMapper {
    public ActivityCategoryInfoResponseDto toDto(ActivityCategory category, Long count) {
        return ActivityCategoryInfoResponseDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .isPublished(category.isPublished())
                .count(count)
                .build();
    }
}
