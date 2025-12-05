package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
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
        return user.getGeneration()
                .stream()
                .map(gen -> gen.getPosition().name())
                .toList();
    }

    private List<String> mapGenerations(User user) {
        return user.getGeneration()
                .stream()
                .map(gen -> gen.getGeneration().getLabel())
                .toList();
    }
}
