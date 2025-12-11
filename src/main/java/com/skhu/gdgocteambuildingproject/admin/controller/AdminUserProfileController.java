package com.skhu.gdgocteambuildingproject.admin.controller;

import com.skhu.gdgocteambuildingproject.admin.api.AdminUserProfileApi;
import com.skhu.gdgocteambuildingproject.admin.dto.*;
import com.skhu.gdgocteambuildingproject.admin.service.AdminUserProfileService;
import com.skhu.gdgocteambuildingproject.admin.service.AdminUserProfileServiceImpl;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/approved")
@PreAuthorize("hasAnyRole('SKHU_ADMIN')")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminUserProfileController implements AdminUserProfileApi {

    private static final ResponseEntity<Void> NO_CONTENT = ResponseEntity.noContent().build();

    private final AdminUserProfileService adminUserProfileService;

    @Override
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

    @Override
    @PostMapping("/ban/{userId}")
    public ResponseEntity<Void> banUser(@PathVariable Long userId,
                                        @RequestBody UserBanRequestDto dto) {
        adminUserProfileService.banUser(userId, dto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/unban/{userId}")
    public ResponseEntity<Void> unbanUser(@PathVariable Long userId) {
        adminUserProfileService.unbanUser(userId);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/search-name")
    public ResponseEntity<ApproveUserInfoPageResponseDto> searchUsersByName(
            @RequestParam String name,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_ORDER) SortOrder order
    ) {
        ApproveUserInfoPageResponseDto response =
                adminUserProfileService.searchUsersByName(name, page, size, sortBy, order);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/search-part")
    public ResponseEntity<ApproveUserInfoPageResponseDto> searchUsersByPart(
            @RequestParam Part part,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_ORDER) SortOrder order
    ) {
        ApproveUserInfoPageResponseDto response =
                adminUserProfileService.searchUsersByPart(part, page, size, sortBy, order);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/search-school")
    public ResponseEntity<ApproveUserInfoPageResponseDto> searchUsersBySchool(
            @RequestParam String school,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_ORDER) SortOrder order
    ) {
        ApproveUserInfoPageResponseDto response =
                adminUserProfileService.searchUsersBySchool(school, page, size, sortBy, order);

        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{generationId}")
    public ResponseEntity<Void> deleteUserGeneration(@PathVariable Long generationId) {
        adminUserProfileService.deleteUserGeneration(generationId);
        return NO_CONTENT;
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<ApprovedUserInfoResponseDto> getApproveUser(@PathVariable Long userId) {
        ApprovedUserInfoResponseDto response = adminUserProfileService.getApproveUser(userId);
        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateApproveUser(@PathVariable Long userId,
                                                  @RequestBody ApproveUserUpdateRequestDto dto) {
        adminUserProfileService.updateApproveUser(userId, dto);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/select-options")
    public ResponseEntity<UserSelectOptionsDto> getUserSelectOptions() {
        UserSelectOptionsDto response = adminUserProfileService.getUserSelectOptions();
        return ResponseEntity.ok(response);
    }
}
