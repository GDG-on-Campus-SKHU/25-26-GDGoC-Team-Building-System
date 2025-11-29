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
public class MyPageServiceImpl implements MypageService {

    private final UserRepository userRepository;
    private final ProfileInfoMapper profileInfoMapper;

    @Override
    @Transactional(readOnly = true)
    public ProfileInfoResponseDto getProfileByUserId(Long userId) {
        User user = findUserBy(userId);
        return profileInfoMapper.map(user);
    }

    @Override
    @Transactional
    public ProfileInfoResponseDto updateProfile(Long userId, ProfileInfoRequestDto requestDto) {
        User user = findUserBy(userId);
        user.updateUserIntroduction(requestDto.introduction());

        List<TechStack> newTechStack = profileInfoMapper.toTechStacks(user, requestDto);
        user.updateTechStacks(newTechStack);

        List<UserLink> newUserLink = profileInfoMapper.toUserLinks(user, requestDto);
        user.updateUserLinks(newUserLink);

        return profileInfoMapper.map(user);
    }

    private User findUserBy(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXIST.getMessage()));
    }
}
