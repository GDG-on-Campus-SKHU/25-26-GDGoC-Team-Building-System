package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserGenerationResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.profile.UserProfileResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.TechStackDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.UserLinkDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class UserProfileInfoMapper {
    public UserProfileResponseDto toDto(User user) {
        return UserProfileResponseDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .school(user.getSchool())
                .part(user.getPart())
                .generations(mapGenerations(user))
                .introduction(user.getIntroduction())
                .techStacks(convertTechStacks(user))
                .userLinks(convertUserLinks(user))
                .build();
    }

    private List<TechStackDto> convertTechStacks(User user) {
        return Optional.ofNullable(user.getTechStacks())
                .map(list -> list.stream().map(TechStackDto::from)
                        .toList())
                .orElse(List.of());
    }

    private List<UserLinkDto> convertUserLinks(User user) {
        return Optional.ofNullable(user.getUserLinks())
                .map(list -> list.stream().map(UserLinkDto::from)
                        .toList())
                .orElse(List.of());
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
