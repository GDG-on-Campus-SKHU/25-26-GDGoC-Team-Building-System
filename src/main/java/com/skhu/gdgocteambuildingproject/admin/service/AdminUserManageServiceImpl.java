package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.PageInfo;
import com.skhu.gdgocteambuildingproject.admin.dto.UserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.UserResponseDto;
import com.skhu.gdgocteambuildingproject.admin.exception.AdminUserManageNotExistException;
import com.skhu.gdgocteambuildingproject.admin.model.UserInfoMapper;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor()
public class AdminUserManageServiceImpl implements AdminUserManageService {

    private final UserRepository userRepository;
    private final UserInfoMapper userInfoMapper;

    @Override
    @Transactional
    public void approveUser(Long userId) {
        User user = findUserByIdOrThrow(userId);

        if (user.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
            throw new IllegalArgumentException(ExceptionMessage.USER_ALREADY_APPROVED.getMessage());
        }

        user.approve();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void rejectUser(Long userId) {
        User user = findUserByIdOrThrow(userId);

        if (user.getApprovalStatus().equals(ApprovalStatus.REJECTED)) {
            throw new IllegalStateException(ExceptionMessage.USER_ALREADY_REJECTED.getMessage());
        }

        user.reject();
        userRepository.save(user);
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

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AdminUserManageNotExistException(ExceptionMessage.USER_NOT_FOUND));
    }
}
