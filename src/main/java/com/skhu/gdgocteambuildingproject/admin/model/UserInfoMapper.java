package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.UserResponseDto;
import com.skhu.gdgocteambuildingproject.admin.util.GenerationMapper;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInfoMapper {

    private final GenerationMapper generationMapper;

    public UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .userName(user.getName())
                .part(user.getPart())
                .generations(generationMapper.toMainGenerationDtos(user))
                .school(user.getSchool())
                .approvalStatus(user.getApprovalStatus())
                .email(user.getEmail())
                .number(user.getNumber())
                .build();
    }
}
