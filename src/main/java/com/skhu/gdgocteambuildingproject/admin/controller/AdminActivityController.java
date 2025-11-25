package com.skhu.gdgocteambuildingproject.admin.controller;

import com.skhu.gdgocteambuildingproject.admin.api.AdminActivityControllerApi;
import com.skhu.gdgocteambuildingproject.admin.dto.activity.ActivitySaveRequestDto;
import com.skhu.gdgocteambuildingproject.admin.dto.activity.PostResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.activity.PostSaveDto;
import com.skhu.gdgocteambuildingproject.admin.service.AdminActivityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
}
