package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.activity.*;

import java.util.List;

public interface AdminActivityService {
    void createActivity(ActivitySaveRequestDto requestDto);

    PostResponseDto updateActivityPost(Long postId, PostSaveDto requestDto);

    List<ActivityCategoryInfoResponseDto> getCategoryInfos();

    List<ActivityResponseDto> getActivitiesByCategory(Long categoryId);

    void deleteActivityPost(Long postId);

    void deleteCategory(Long categoryId);

    void updateCategoryTitleAndStatus(Long categoryId, ActivityUpdateRequestDto dto);

    PostResponseDto getActivityPost(Long postId);
}
