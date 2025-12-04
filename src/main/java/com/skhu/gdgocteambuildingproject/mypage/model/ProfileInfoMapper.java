package com.skhu.gdgocteambuildingproject.mypage.model;

import com.skhu.gdgocteambuildingproject.mypage.dto.TechStackDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.UserLinkDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoUpdateRequestDto;
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
                .name(user.getName())
                .school(user.getSchool())
                .role(user.getRole())
                .part(user.getPart())
                .introduction(user.getIntroduction())
                .techStacks(user.getTechStacks().stream()
                        .map(TechStackDto::from)
                        .toList())
                .userLinks(user.getUserLinks().stream()
                        .map(UserLinkDto::from)
                        .toList())
                .build();
    }

    public List<TechStack> toTechStacks(User user, ProfileInfoUpdateRequestDto requestDto) {
        List<TechStackDto> techStackDtos = getTechStackDtosOrEmpty(requestDto);

        return techStackDtos.stream()
                .map(dto -> TechStack.builder()
                        .techStackType(dto.techStackType())
                        .user(user)
                        .build())
                .toList();
    }

    public List<UserLink> toUserLinks(User user, ProfileInfoUpdateRequestDto requestDto) {
        List<UserLinkDto> userLinkDtos = getUserLinkDtosOrEmpty(requestDto);

        return userLinkDtos.stream()
                .map(dto -> UserLink.builder()
                        .linkType(dto.linkType())
                        .url(dto.url())
                        .user(user)
                        .build())
                .toList();
    }

    private List<TechStackDto> getTechStackDtosOrEmpty(ProfileInfoUpdateRequestDto requestDto) {
        List<TechStackDto> techStackDtos = requestDto.techStacks();
        return techStackDtos == null ? List.of() : techStackDtos;
    }

    private List<UserLinkDto> getUserLinkDtosOrEmpty(ProfileInfoUpdateRequestDto requestDto) {
        List<UserLinkDto> userLinkDtos = requestDto.userLinks();
        return userLinkDtos == null ? List.of() : userLinkDtos;
    }
}
