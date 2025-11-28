package com.skhu.gdgocteambuildingproject.admin.controller;

import com.skhu.gdgocteambuildingproject.admin.api.AdminActivityControllerApi;
import com.skhu.gdgocteambuildingproject.admin.dto.activity.*;
import com.skhu.gdgocteambuildingproject.admin.service.AdminActivityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/activity")
@PreAuthorize("hasAnyRole('SKHU_ADMIN')")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminActivityController implements AdminActivityControllerApi {

    private final AdminActivityService adminActivityService;

    @Override
    @PostMapping
    public ResponseEntity<Void> createActivity(@RequestBody ActivitySaveRequestDto activitySaveRequestDto) {
        adminActivityService.createActivity(activitySaveRequestDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updateActivityPost(@PathVariable Long postId,
                                                              @RequestBody PostSaveDto postSaveDto) {
        PostResponseDto postResponseDto =
                adminActivityService.updateActivityPost(postId, postSaveDto);
        return ResponseEntity.ok(postResponseDto);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ActivityCategoryInfoResponseDto>> getCategoryInfos() {
        List<ActivityCategoryInfoResponseDto> categoryInfo = adminActivityService.getCategoryInfos();
        return ResponseEntity.ok().body(categoryInfo);
    }

    @Override
    @GetMapping("/{categoryId}")
    public ResponseEntity<List<ActivityResponseDto>> getActivitiesByCategory(@PathVariable Long categoryId) {
        List<ActivityResponseDto> activitiesByCategory = adminActivityService.getActivitiesByCategory(categoryId);
        return ResponseEntity.ok().body(activitiesByCategory);
    }
}
