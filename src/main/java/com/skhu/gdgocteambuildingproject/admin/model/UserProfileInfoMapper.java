package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserGenerationResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.profile.UserProfileResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.TechStackResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.UserLinkResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

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

    private List<TechStackResponseDto> convertTechStacks(User user) {
        return user.getTechStacks().stream()
                .map(TechStackResponseDto::from)
                .toList();
    }
    private List<UserLinkResponseDto> convertUserLinks(User user) {
        return user.getUserLinks().stream()
                .map(UserLinkResponseDto::from)
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
