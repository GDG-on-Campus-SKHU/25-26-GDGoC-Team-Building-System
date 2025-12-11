package com.skhu.gdgocteambuildingproject.mypage.model;

import com.skhu.gdgocteambuildingproject.mypage.dto.TechStackDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.UserLinkDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.UserGenerationResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileInfoMapper {

    public ProfileInfoResponseDto toDto(User user) {
        return ProfileInfoResponseDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .school(user.getSchool())
                .generations(mapGenerations(user))
                .part(user.getPart())
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

    private List<UserGenerationResponseDto> mapGenerations(User user) {
        return user.getGeneration().stream()
                .map(gen -> new UserGenerationResponseDto(
                        gen.getId(),
                        gen.getGeneration().getLabel(),
                        gen.getPosition().name(),
                        gen.isMain()
                ))
                .toList();
    }
}
