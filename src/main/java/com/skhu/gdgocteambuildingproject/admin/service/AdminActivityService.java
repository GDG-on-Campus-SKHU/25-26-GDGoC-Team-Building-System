package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.activity.ActivitySaveRequestDto;
import com.skhu.gdgocteambuildingproject.admin.dto.activity.PostResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.activity.PostSaveDto;

public interface AdminActivityService {
    void createActivity(ActivitySaveRequestDto requestDto);

    PostResponseDto updateActivityPost(Long postId, PostSaveDto requestDto);
}
