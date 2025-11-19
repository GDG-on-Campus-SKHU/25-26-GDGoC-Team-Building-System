package com.skhu.gdgocteambuildingproject.admin.controller;

import com.skhu.gdgocteambuildingproject.admin.api.AdminUserProfileApi;
import com.skhu.gdgocteambuildingproject.admin.dto.ApproveUserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.service.AdminUserProfileServiceImpl;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/approved")
@PreAuthorize("hasAnyRole('SKHU_ADMIN')")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminUserProfileController implements AdminUserProfileApi {

    private final AdminUserProfileServiceImpl adminUserProfileService;

    @GetMapping("/users")
    public ResponseEntity<ApproveUserInfoPageResponseDto> getApproveUsers(
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_ORDER) SortOrder order
    ) {
        ApproveUserInfoPageResponseDto approveAllUsers =
                adminUserProfileService.getApproveUsers(page, size, sortBy, order);

        return ResponseEntity.ok(approveAllUsers);
    }
}
