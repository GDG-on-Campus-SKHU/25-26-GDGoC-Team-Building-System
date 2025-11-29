package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.ApproveUserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.UserBanRequestDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;

public interface AdminUserProfileService {

    ApproveUserInfoPageResponseDto getApproveUsers(
            int page,
            int size,
            String sortBy,
            SortOrder order
    );

    void banUser(Long userId, UserBanRequestDto dto);

    void unbanUser(Long userId);
}
