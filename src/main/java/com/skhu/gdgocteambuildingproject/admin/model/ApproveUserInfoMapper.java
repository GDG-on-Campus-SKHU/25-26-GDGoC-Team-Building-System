package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserGenerationResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserResponseDto;
import com.skhu.gdgocteambuildingproject.admin.util.GenerationMapper;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApproveUserInfoMapper {

    private final GenerationMapper generationMapper;

    public ApprovedUserResponseDto toApprovedUserResponseDto(User user) {

        if (user.isDeleted()) {
            return buildDeletedUserDto(user);
        }

        return ApprovedUserResponseDto.builder()
                .id(user.getId())
                .userName(user.getName())
                .part(user.getPart())
                .school(user.getSchool())
                .generations(generationMapper.toMainGenerationDtos(user))
                .build();
    }

    private ApprovedUserResponseDto buildDeletedUserDto(User user) {
        return ApprovedUserResponseDto.builder()
                .id(user.getId())
                .userName(user.getName())
                .build();
    }
}
