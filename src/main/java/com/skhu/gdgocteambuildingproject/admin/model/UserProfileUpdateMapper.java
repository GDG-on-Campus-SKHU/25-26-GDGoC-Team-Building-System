package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.profile.UpdateUserProfileRequestDto;
import com.skhu.gdgocteambuildingproject.user.domain.TechStack;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.UserLink;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserProfileUpdateMapper {
    public List<TechStack> toTechStacks(User user, UpdateUserProfileRequestDto requestDto) {
        return Optional.ofNullable(requestDto.techStacks())
                .orElseGet(List::of)
                .stream()
                .map(dto -> TechStack.builder()
                        .techStackType(dto.techStackType())
                        .user(user)
                        .build())
                .toList();
    }

    public List<UserLink> toUserLinks(User user, UpdateUserProfileRequestDto requestDto) {
        return Optional.ofNullable(requestDto.userLinks())
                .orElseGet(List::of)
                .stream()
                .map(dto -> UserLink.builder()
                        .linkType(dto.linkType())
                        .url(dto.url())
                        .user(user)
                        .build())
                .toList();
    }
}
