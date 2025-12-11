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
        return user.getTechStacks().stream()
                .map(TechStackDto::from)
                .toList();
    }
    private List<UserLinkDto> convertUserLinks(User user) {
        return user.getUserLinks().stream()
                .map(UserLinkDto::from)
                .toList();
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
