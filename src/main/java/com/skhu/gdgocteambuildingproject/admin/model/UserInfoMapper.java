package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.UserResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserInfoMapper {
    public UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .userName(user.getName())
                .part(user.getPart())
                .generation(user.getGeneration())
                .school(user.getSchool())
                .approvalStatus(user.getApprovalStatus())
                .email(user.getEmail())
                .number(user.getNumber())
                .build();
    }
}
