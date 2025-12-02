package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApproveUserInfoMapper {
    public ApprovedUserResponseDto toApprovedUserResponseDto(User user) {
        return ApprovedUserResponseDto.builder()
                .id(user.getId())
                .userPosition(mapPositions(user))
                .userName(user.getName())
                .part(user.getPart())
                .school(user.getSchool())
                .generation(mapGenerations(user))
                .build();
    }

    private List<String> mapPositions(User user) {
        return user.getPositions()
                .stream()
                .map(UserPosition::name)
                .toList();
    }

    private List<String> mapGenerations(User user) {
        return user.getGenerations()
                .stream()
                .map(Generation::getLabel)
                .toList();
    }
}
