package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.ApproveUserInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.PageInfo;
import com.skhu.gdgocteambuildingproject.admin.model.ApproveUserInfoMapper;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminUserProfileServiceImpl implements AdminUserProfileService {

    private final UserRepository userRepository;
    private final ApproveUserInfoMapper approveUserInfoMapper;

    public ApproveUserInfoPageResponseDto getApproveAllUsers(int page, int size, String sortBy, SortOrder order) {
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
}
