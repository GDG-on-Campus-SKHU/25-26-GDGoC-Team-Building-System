package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.ApproveUserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.PageInfo;
import com.skhu.gdgocteambuildingproject.admin.model.ApproveUserInfoMapper;
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
    @Transactional
    public ApproveUserInfoPageResponseDto getApproveUsers(int page, int size, String sortBy, SortOrder order) {
        Pageable pageable = PageRequest.of(page, size, order.sort(sortBy));

        Page<User> userPage = userRepository.findAllByApprovalStatus(ApprovalStatus.APPROVED, pageable);

        List<ApprovedUserResponseDto> userResponseDtos = userPage
                .stream()
                .map(approveUserInfoMapper::toApprovedUserResponseDto)
                .toList();

        return ApproveUserInfoPageResponseDto.builder()
                .users(userResponseDtos)
                .pageInfo(PageInfo.from(userPage))
                .build();
    }

    @Override
    @Transactional
    public void banUser(Long userId) {
        User user = getUserOrThrow(userId);

        if (user.getUserStatus() == UserStatus.BANNED) {
            throw new IllegalStateException(ExceptionMessage.ALREADY_BANNED_USER.getMessage());
        }

        user.ban();
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

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND.getMessage()));
    }
}
