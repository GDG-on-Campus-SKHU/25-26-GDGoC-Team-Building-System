package com.skhu.gdgocteambuildingproject.mypage.model;

import com.skhu.gdgocteambuildingproject.mypage.dto.request.TechStackDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.UserLinkDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class ProfileInfoMapper {

    public ProfileInfoResponseDto map(User user) {
        return ProfileInfoResponseDto.builder()
                .userId(user.getId())
                .school(user.getSchool())
                .role(user.getRole())
                .part(user.getPart())
                .introduction(user.getIntroduction())
                .techStacks(user.getTechStacks().stream()
                        .map(TechStackDto::toEntity)
                        .toList())
                .userLinks(user.getUserLinks().stream()
                        .map(UserLinkDto::toEntity)
                        .toList())
                .build();
    }
}
