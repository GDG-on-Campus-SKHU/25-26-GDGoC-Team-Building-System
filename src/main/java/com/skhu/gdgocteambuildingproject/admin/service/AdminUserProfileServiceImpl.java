package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.ApproveUserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.PageInfo;
import com.skhu.gdgocteambuildingproject.admin.dto.UserBanRequestDto;
import com.skhu.gdgocteambuildingproject.admin.model.ApproveUserInfoMapper;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserStatus;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminUserProfileServiceImpl implements AdminUserProfileService {

    private final UserRepository userRepository;
    private final ApproveUserInfoMapper approveUserInfoMapper;

    @Override
    @Transactional(readOnly = true)
    public ApproveUserInfoPageResponseDto getApproveUsers(int page, int size, String sortBy, SortOrder order) {
        Pageable pageable = createPageable(page, size, sortBy, order);
        Page<User> userPage = userRepository.findAllByApprovalStatus(ApprovalStatus.APPROVED, pageable);

        return toPageResponse(userPage);
    }

    @Override
    @Transactional
    public void banUser(Long userId, UserBanRequestDto dto) {
        User user = getUserOrThrow(userId);

        if (user.getUserStatus() == UserStatus.BANNED) {
            throw new IllegalStateException(ExceptionMessage.ALREADY_BANNED_USER.getMessage());
        }

        user.ban(dto.reason());
    }

    @Override
    @Transactional
    public void unbanUser(Long userId) {
        User user = getUserOrThrow(userId);

        if (user.getUserStatus() == UserStatus.ACTIVE) {
            throw new IllegalStateException(ExceptionMessage.ALREADY_ACTIVE_USER.getMessage());
        }

        user.unban();
    }

    @Override
    @Transactional(readOnly = true)
    public ApproveUserInfoPageResponseDto searchUsersByName(String name, int page, int size, String sortBy, SortOrder order) {
        Pageable pageable = createPageable(page, size, sortBy, order);
        Page<User> userPage = userRepository.findByNameContainingAndApprovalStatus(
                name,
                ApprovalStatus.APPROVED,
                pageable
        );

        return toPageResponse(userPage);
    }

    @Override
    @Transactional(readOnly = true)
    public ApproveUserInfoPageResponseDto searchUsersByPart(Part part, int page, int size, String sortBy, SortOrder order) {
        Pageable pageable = createPageable(page, size, sortBy, order);
        Page<User> userPage = userRepository.findByPartAndApprovalStatus(
                part,
                ApprovalStatus.APPROVED,
                pageable
        );

        return toPageResponse(userPage);
    }

    @Override
    @Transactional(readOnly = true)
    public ApproveUserInfoPageResponseDto searchUsersBySchool(String school, int page, int size, String sortBy, SortOrder order) {
        Pageable pageable = createPageable(page, size, sortBy, order);
        Page<User> userPage = userRepository.findBySchoolContainingAndApprovalStatus(
                school,
                ApprovalStatus.APPROVED,
                pageable
        );

        return toPageResponse(userPage);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND.getMessage()));
    }

    private ApproveUserInfoPageResponseDto toPageResponse(Page<User> userPage) {
        List<ApprovedUserResponseDto> users = userPage.getContent().stream()
                .map(approveUserInfoMapper::toApprovedUserResponseDto)
                .toList();

        return ApproveUserInfoPageResponseDto.builder()
                .users(users)
                .pageInfo(PageInfo.from(userPage))
                .build();
    }

    private Pageable createPageable(int page, int size, String sortBy, SortOrder order) {
        return PageRequest.of(page, size, order.sort(sortBy));
    }
}
