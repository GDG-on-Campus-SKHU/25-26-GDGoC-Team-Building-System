package com.skhu.gdgocteambuildingproject.activity.service;

import com.skhu.gdgocteambuildingproject.activity.domain.Activity;
import com.skhu.gdgocteambuildingproject.activity.domain.ActivityCategory;
import com.skhu.gdgocteambuildingproject.activity.dto.ActivityInfoResponseDto;
import com.skhu.gdgocteambuildingproject.activity.dto.ActivitySummaryResponseDto;
import com.skhu.gdgocteambuildingproject.activity.model.ActivityInfoMapper;
import com.skhu.gdgocteambuildingproject.activity.repository.ActivityCategoryRepository;
import com.skhu.gdgocteambuildingproject.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityCategoryRepository activityCategoryRepository;
    private final ActivityInfoMapper activityInfoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ActivitySummaryResponseDto> getAllActivities() {
        List<ActivityCategory> categories = activityCategoryRepository.findAllByPublishedTrue();

        return categories.stream()
                .map(this::getSummary)
                .toList();
    }

    private ActivitySummaryResponseDto getSummary(ActivityCategory activityCategory) {
        List<Activity> activities = activityRepository.findByActivityCategory(activityCategory);
        List<ActivityInfoResponseDto> infoResponseDtos = activityInfoMapper.mapToInfoList(activities);

        return activityInfoMapper.mapToSummary(activityCategory, infoResponseDtos);
    }
}
