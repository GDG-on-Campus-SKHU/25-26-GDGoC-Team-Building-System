package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.ApproveUserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;

public interface AdminUserProfileService {

    ApproveUserInfoPageResponseDto getApproveAllUsers(
            int page,
            int size,
            String sortBy,
            SortOrder order
    );
}
