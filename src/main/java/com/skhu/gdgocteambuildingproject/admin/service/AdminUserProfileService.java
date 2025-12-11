package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.*;
import com.skhu.gdgocteambuildingproject.admin.dto.profile.UpdateUserProfileRequestDto;
import com.skhu.gdgocteambuildingproject.admin.dto.profile.UserProfileResponseDto;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
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

    ApproveUserInfoPageResponseDto searchUsersByName(
            String name,
            int page,
            int size,
            String sortBy,
            SortOrder order
    );

    ApproveUserInfoPageResponseDto searchUsersByPart(
            Part part,
            int page,
            int size,
            String sortBy,
            SortOrder order
    );

    ApproveUserInfoPageResponseDto searchUsersBySchool(
            String school,
            int page,
            int size,
            String sortBy,
            SortOrder order
    );

    void deleteUserGeneration(Long generationId);

    ApprovedUserInfoResponseDto getApproveUser(Long userId);

    void updateApproveUser(Long userId, ApproveUserUpdateRequestDto dto);

    UserSelectOptionsDto getUserSelectOptions();

    UserProfileResponseDto getProfileByUserid(Long userId);

    void updateProfileByUser(Long userId, UpdateUserProfileRequestDto dto);
}
