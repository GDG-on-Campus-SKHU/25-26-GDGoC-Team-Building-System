package com.skhu.gdgocteambuildingproject.mypage.service;

import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.model.ProfileInfoMapper;
import com.skhu.gdgocteambuildingproject.user.domain.TechStack;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.UserLink;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final ProfileInfoMapper profileInfoMapper;

    @Transactional(readOnly = true)
    public ProfileInfoResponseDto getProfileByUserId(Long userId) {
        User user = findUserBy(userId);
        return profileInfoMapper.map(user);
    }

    @Transactional
    public ProfileInfoResponseDto updateProfile(Long userId, ProfileInfoRequestDto requestDto) {
        User user = findUserBy(userId);
        user.updateBasicProfile(
                requestDto.school(),
                requestDto.role(),
                requestDto.part(),
                requestDto.introduction()
        );

        List<TechStack> newTechStack = createTechStacks(user, requestDto);
        user.updateTechStacks(newTechStack);

        List<UserLink> newUserLink = createUserLinks(user, requestDto);
        user.updateUserLinks(newUserLink);

        return profileInfoMapper.map(user);
    }

    private List<TechStack> createTechStacks(User user, ProfileInfoRequestDto requestDto) {
        return requestDto.techStacks().stream()
                .map(dto -> TechStack.builder()
                        .techStackType(dto.techStackType())
                        .user(user)
                        .build())
                .toList();
    }

    private List<UserLink> createUserLinks(User user, ProfileInfoRequestDto requestDto) {
        return requestDto.userLinks().stream()
                .map(dto -> UserLink.builder()
                        .linkType(dto.linkType())
                        .url(dto.url())
                        .user(user)
                        .build())
                .toList();
    }

    private User findUserBy(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXIST.getMessage()));
    }
}
