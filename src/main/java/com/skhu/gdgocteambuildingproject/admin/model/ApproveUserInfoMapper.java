package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class ApproveUserInfoMapper {
    public ApprovedUserResponseDto toApprovedUserResponseDto(User user) {
        return ApprovedUserResponseDto.builder()
                .id(user.getId())
                .userPosition(user.getPosition())
                .userName(user.getName())
                .part(user.getPart())
                .school(user.getSchool())
                .generation(user.getGeneration())
                .build();
    }
}
