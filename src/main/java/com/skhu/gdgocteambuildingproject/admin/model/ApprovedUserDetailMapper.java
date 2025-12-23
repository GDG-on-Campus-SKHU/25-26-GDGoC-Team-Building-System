package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserGenerationResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserInfoResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApprovedUserDetailMapper {

    public ApprovedUserInfoResponseDto toDto(User user) {

        UserStatus userStatus = resolveUser(user);

        if (userStatus == UserStatus.DELETED) {
            return toDeletedDto(user);
        }

        if (userStatus == UserStatus.BANNED) {
            return toBannedDto(user);
        }

        return toActiveDto(user, userStatus);
    }

    private ApprovedUserInfoResponseDto toDeletedDto(User user) {
        return ApprovedUserInfoResponseDto.builder()
                .name(user.getName())
                .approveAt(user.getApprovedAt())
                .status(UserStatus.DELETED)
                .deletedAt(user.getDeletedAt())
                .build();
    }

    private ApprovedUserInfoResponseDto toBannedDto(User user) {
        return ApprovedUserInfoResponseDto.builder()
                .name(user.getName())
                .status(UserStatus.BANNED)
                .bannedAt(user.getBannedAt())
                .unbannedAt(user.getUnbannedAt())
                .banReason(user.getBanReason())
                .email(user.getEmail())
                .phoneNum(user.getNumber())
                .approveAt(user.getApprovedAt())
                .part(user.getPart())
                .generations(mapGenerations(user))
                .school(user.getSchool())
                .build();
    }

    private ApprovedUserInfoResponseDto toActiveDto(User user, UserStatus status) {
        return ApprovedUserInfoResponseDto.builder()
                .name(user.getName())
                .status(status)
                .email(user.getEmail())
                .phoneNum(user.getNumber())
                .approveAt(user.getApprovedAt())
                .part(user.getPart())
                .generations(mapGenerations(user))
                .bannedAt(user.getBannedAt())
                .unbannedAt(user.getUnbannedAt())
                .school(user.getSchool())
                .build();
    }

    private UserStatus resolveUser(User user) {
        if (user.isDeleted()) {
            return UserStatus.DELETED;
        }
        return user.getUserStatus();
    }

    private List<ApprovedUserGenerationResponseDto> mapGenerations(User user) {
        return user.getGeneration().stream()
                .map(gen -> new ApprovedUserGenerationResponseDto(
                        gen.getId(),
                        gen.getGeneration().getLabel(),
                        gen.getPosition().name(),
                        gen.isMain()
                ))
                .toList();
    }
}
