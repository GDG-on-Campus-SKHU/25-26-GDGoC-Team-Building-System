package com.skhu.gdgocteambuildingproject.activity.controller;

import com.skhu.gdgocteambuildingproject.activity.api.ActivityControllerApi;
import com.skhu.gdgocteambuildingproject.activity.dto.ActivitySummaryResponseDto;
import com.skhu.gdgocteambuildingproject.activity.service.ActivityServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityController implements ActivityControllerApi {

    private final ActivityServiceImpl activityServiceImpl;

    @Override
    @GetMapping("/published")
    public ResponseEntity<List<ActivitySummaryResponseDto>> getAllActivity() {
        return ResponseEntity.ok(activityServiceImpl.getAllActivities());
    }
}
