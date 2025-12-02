package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.UserResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInfoMapper {
    public UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .userName(user.getName())
                .part(user.getPart())
                .generation(mapGenerations(user))
                .school(user.getSchool())
                .approvalStatus(user.getApprovalStatus())
                .email(user.getEmail())
                .number(user.getNumber())
                .build();
    }

    private List<String> mapGenerations(User user) {
        return user.getGenerations()
                .stream()
                .map(Generation::getLabel)
                .toList();
    }
}
