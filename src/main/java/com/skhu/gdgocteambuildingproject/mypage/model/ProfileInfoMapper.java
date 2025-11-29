package com.skhu.gdgocteambuildingproject.mypage.model;

import com.skhu.gdgocteambuildingproject.mypage.dto.TechStackDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.UserLinkDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.TechStack;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.UserLink;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileInfoMapper {

    public ProfileInfoResponseDto map(User user) {
        return ProfileInfoResponseDto.builder()
                .userId(user.getId())
                .introduction(user.getIntroduction())
                .techStacks(user.getTechStacks().stream()
                        .map(TechStackDto::from)
                        .toList())
                .userLinks(user.getUserLinks().stream()
                        .map(UserLinkDto::from)
                        .toList())
                .build();
    }

    public List<TechStack> toTechStacks(User user, ProfileInfoRequestDto requestDto) {
        return requestDto.techStacks().stream()
                .map(dto -> TechStack.builder()
                        .techStackType(dto.techStackType())
                        .user(user)
                        .build())
                .toList();
    }

    public List<UserLink> toUserLinks(User user, ProfileInfoRequestDto requestDto) {
        return requestDto.userLinks().stream()
                .map(dto -> UserLink.builder()
                        .linkType(dto.linkType())
                        .url(dto.url())
                        .user(user)
                        .build())
                .toList();
    }
}
