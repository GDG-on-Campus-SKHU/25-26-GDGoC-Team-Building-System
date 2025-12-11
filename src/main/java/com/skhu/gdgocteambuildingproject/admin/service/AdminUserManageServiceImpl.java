package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.PageInfo;
import com.skhu.gdgocteambuildingproject.admin.dto.UserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.UserResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.UserSearchResponseDto;
import com.skhu.gdgocteambuildingproject.admin.exception.AdminUserManageNotExistException;
import com.skhu.gdgocteambuildingproject.admin.model.UserInfoMapper;
import com.skhu.gdgocteambuildingproject.admin.model.UserSearchMapper;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserStatus;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
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
public class AdminUserManageServiceImpl implements AdminUserManageService {

    private final UserRepository userRepository;
    private final UserInfoMapper userInfoMapper;
    private final UserSearchMapper userSearchMapper;

    @Override
    @Transactional
    public void approveUser(Long userId) {
        User user = findUserByIdOrThrow(userId);

        if (user.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
            throw new IllegalArgumentException(ExceptionMessage.USER_ALREADY_APPROVED.getMessage());
        }

        user.approve();
    }

    @Override
    @Transactional
    public void rejectUser(Long userId) {
        User user = findUserByIdOrThrow(userId);

        if (user.getApprovalStatus().equals(ApprovalStatus.REJECTED)) {
            throw new IllegalStateException(ExceptionMessage.USER_ALREADY_REJECTED.getMessage());
        }

        user.reject();
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoPageResponseDto getAllUsers(int page, int size, String sortBy, SortOrder order) {
        Pageable pageable = PageRequest.of(page, size, order.sort(sortBy));

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDto> userResponseDtos = userPage
                .stream()
                .map(userInfoMapper::toUserResponseDto)
                .toList();

        return UserInfoPageResponseDto.builder()
                .users(userResponseDtos)
                .pageInfo(PageInfo.from(userPage))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSearchResponseDto> searchUsers(
            String generationLabel,
            List<String> schools
    ) {
        Generation generation = getGenerationFromLabel(generationLabel);

        List<User> users = userRepository.searchUsers(
                generation,
                schools,
                ApprovalStatus.APPROVED,
                UserStatus.ACTIVE
        );

        return users.stream()
                .map(user -> userSearchMapper.map(user, generationLabel))
                .toList();
    }

    @Override
    @Transactional
    public void resetRejectedUser(Long userId) {
        User user = findUserByIdOrThrow(userId);

        if (user.getApprovalStatus() != ApprovalStatus.REJECTED) {
            throw new IllegalStateException(ExceptionMessage.USER_NOT_REJECTED.getMessage());
        }

        user.resetToWaiting();
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AdminUserManageNotExistException(ExceptionMessage.USER_NOT_FOUND));
    }

    private Generation getGenerationFromLabel(String label) {
        if (label == null) {
            return null;
        }

        return Generation.fromLabel(label);
    }
}
