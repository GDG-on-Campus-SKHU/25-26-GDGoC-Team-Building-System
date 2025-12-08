package com.skhu.gdgocteambuildingproject.activity.service;

import com.skhu.gdgocteambuildingproject.activity.dto.ActivitySummaryResponseDto;

import java.util.List;

public interface ActivityService {

    List<ActivitySummaryResponseDto> getAllActivities();
}
