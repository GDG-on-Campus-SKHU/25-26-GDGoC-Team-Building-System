package com.skhu.gdgocteambuildingproject.mypage.service;

import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaMember;
import com.skhu.gdgocteambuildingproject.Idea.repository.IdeaMemberRepository;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoUpdateRequestDto;
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

@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {

    private final UserRepository userRepository;
    private final ProfileInfoMapper profileInfoMapper;
    private final IdeaMemberRepository ideaMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public ProfileInfoResponseDto getProfileByUserId(Long userId) {
        User user = findUserByIdOrThrow(userId);
        return profileInfoMapper.map(user);
    }

    @Override
    @Transactional
    public ProfileInfoResponseDto updateUserModifiableProfile(Long userId, ProfileInfoUpdateRequestDto requestDto) {
        User user = findUserByIdOrThrow(userId);
        user.updateUserIntroduction(requestDto.introduction());

        List<TechStack> newTechStacks = profileInfoMapper.toTechStacks(user, requestDto);
        user.updateTechStacks(newTechStacks);

        List<UserLink> newUserLinks = profileInfoMapper.toUserLinks(user, requestDto);
        user.updateUserLinks(newUserLinks);

        return profileInfoMapper.map(user);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileInfoResponseDto getProfileByIdeaMemberId(Long ideaMemberId) {
        IdeaMember ideaMember = ideaMemberRepository.findByIdWithUser(ideaMemberId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.IDEA_MEMBER_NOT_FOUND.getMessage()));

        return profileInfoMapper.map(ideaMember.getUser());
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.USER_NOT_EXIST.getMessage()));
    }
}
