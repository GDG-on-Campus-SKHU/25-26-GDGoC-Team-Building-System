package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.UserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.UserSearchResponseDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;

import java.util.List;

public interface AdminUserManageService {
    void approveUser(Long userId);

    void rejectUser(Long userId);

    UserInfoPageResponseDto getAllUsers(
            int page,
            int size,
            String sortBy,
            SortOrder order
    );

    List<UserSearchResponseDto> searchUsers(
            String generation,
            List<String> schools
    );

    void resetRejectedUser(Long userId);
}
