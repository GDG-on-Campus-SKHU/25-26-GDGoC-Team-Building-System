package com.skhu.gdgocteambuildingproject.admin.controller;

import com.skhu.gdgocteambuildingproject.admin.api.AdminUserManageApi;
import com.skhu.gdgocteambuildingproject.admin.dto.UserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.service.AdminUserManageService;
import com.skhu.gdgocteambuildingproject.admin.service.AdminUserManageServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('SKHU_ADMIN')")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminUserManageController implements AdminUserManageApi {

    private final AdminUserManageServiceImpl adminUserManageService;

    @Override
    @PostMapping("/{userId}/approve")
    public ResponseEntity<Void> approveUser(@PathVariable Long userId) {
        adminUserManageService.approveUser(userId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/{userId}/reject")
    public ResponseEntity<Void> rejectUser(@PathVariable Long userId) {
        adminUserManageService.rejectUser(userId);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/users")
    public ResponseEntity<UserInfoPageResponseDto> getAllUsers(
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_ORDER) SortOrder order
    ) {
        UserInfoPageResponseDto response = adminUserManageService.getAllUsers(page, size, sortBy, order);
        return ResponseEntity.ok(response);
    }
}
