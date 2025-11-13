package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.UserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;

public interface AdminUserManageService {
    void approveUser(Long userId);

    void rejectUser(Long userId);

    UserInfoPageResponseDto getAllUsers(
            int page,
            int size,
            String sortBy,
            SortOrder order
    );
}
