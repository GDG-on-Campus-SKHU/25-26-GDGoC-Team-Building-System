package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.*;
import com.skhu.gdgocteambuildingproject.admin.dto.profile.UpdateUserProfileRequestDto;
import com.skhu.gdgocteambuildingproject.admin.dto.profile.UserProfileResponseDto;
import com.skhu.gdgocteambuildingproject.admin.model.*;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.user.domain.TechStack;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.UserGeneration;
import com.skhu.gdgocteambuildingproject.user.domain.UserLink;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserStatus;
import com.skhu.gdgocteambuildingproject.user.repository.UserGenerationRepository;
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
    private final UserGenerationRepository userGenerationRepository;

    private final ApproveUserInfoMapper approveUserInfoMapper;
    private final UserProfileInfoMapper userProfileInfoMapper;
    private final UserProfileUpdateMapper userProfileUpdateMapper;
    private final ApprovedUserDetailMapper approvedUserDetailMapper;
    private final UserSelectOptionsMapper userSelectOptionsMapper;

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

    @Override
    @Transactional
    public void deleteUserGeneration(Long generationId) {
        UserGeneration userGeneration = userGenerationRepository.findById(generationId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.GENERATION_NOT_FOUND.getMessage()));

        userGenerationRepository.delete(userGeneration);
    }

    @Override
    @Transactional(readOnly = true)
    public ApprovedUserInfoResponseDto getApproveUser(Long userId) {
        User user = getUserOrThrow(userId);

        return approvedUserDetailMapper.toDto(user);
    }

    @Override
    @Transactional
    public void updateApproveUser(Long userId, ApproveUserUpdateRequestDto dto) {
        User user = getUserOrThrow(userId);

        for (UserGenerationUpdateDto generationItem : dto.generations()) {
            processUpdateGeneration(user, generationItem);
        }
        user.updateSchool(dto.school());
        user.updatePart(dto.part());
    }

    @Override
    @Transactional(readOnly = true)
    public UserSelectOptionsDto getUserSelectOptions() {
        return userSelectOptionsMapper.fromEnums();
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponseDto getProfileByUserid(Long userId) {
        User user = getUserOrThrow(userId);

        return userProfileInfoMapper.toDto(user);
    }

    @Override
    @Transactional
    public void updateProfileByUser(Long userId, UpdateUserProfileRequestDto dto) {
        User user = getUserOrThrow(userId);
        user.updateUserIntroduction(dto.introduction());

        List<TechStack> techStacks = userProfileUpdateMapper.toTechStacks(user, dto);
        user.updateTechStacks(techStacks);

        List<UserLink> userLinks = userProfileUpdateMapper.toUserLinks(user, dto);
        user.updateUserLinks(userLinks);
    }

    private void processUpdateGeneration(User user, UserGenerationUpdateDto generationItem) {
        if (generationItem.id() != null) {
            UserGeneration existingGeneration = findExistingGeneration(user, generationItem.id());
            updateExistingGeneration(existingGeneration, generationItem);
        } else {
            createNewGeneration(user, generationItem);
        }
    }

    private UserGeneration findExistingGeneration(User user, Long id) {
        return user.getGeneration().stream()
                .filter(gen -> gen.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.GENERATION_NOT_FOUND.getMessage()));
    }

    private void updateExistingGeneration(UserGeneration userGeneration, UserGenerationUpdateDto dto) {
        userGeneration.updateGeneration(Generation.fromLabel(dto.generation()));
        userGeneration.updatePosition(dto.position());
        userGeneration.updateMain(dto.isMain());
    }

    private void createNewGeneration(User user, UserGenerationUpdateDto dto) {
        UserGeneration newGeneration = UserGeneration.builder()
                .generation(Generation.fromLabel(dto.generation()))
                .position(dto.position())
                .user(user)
                .isMain(dto.isMain())
                .build();

        user.addGeneration(newGeneration);
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
