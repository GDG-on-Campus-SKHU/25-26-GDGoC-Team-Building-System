package com.skhu.gdgocteambuildingproject.mypage.service;

import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProjectExhibitUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.MypageProjectGalleryResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.TechStackOptionsResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.UserLinkOptionsResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.model.MypageProjectGalleryMapper;
import com.skhu.gdgocteambuildingproject.mypage.model.ProfileInfoMapper;
import com.skhu.gdgocteambuildingproject.mypage.model.ProfileInfoUpdateMapper;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectMemberRepository;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.IdeaMember;
import com.skhu.gdgocteambuildingproject.teambuilding.repository.IdeaMemberRepository;
import com.skhu.gdgocteambuildingproject.user.domain.TechStack;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.UserLink;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.LinkType;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.TechStackType;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {

    private final UserRepository userRepository;
    private final IdeaMemberRepository ideaMemberRepository;
    private final GalleryProjectMemberRepository galleryProjectMemberRepository;
    private final ProfileInfoMapper profileInfoMapper;
    private final ProfileInfoUpdateMapper profileInfoUpdateMapper;
    private final MypageProjectGalleryMapper mypageProjectGalleryMapper;


    @Override
    @Transactional(readOnly = true)
    public ProfileInfoResponseDto getProfileByUserPrincipal(Long userId) {
        User user = findUserByIdOrThrow(userId);

        return profileInfoMapper.toDto(user);
    }

    @Override
    @Transactional
    public ProfileInfoResponseDto updateUserModifiableProfile(Long userId, ProfileInfoUpdateRequestDto requestDto) {
        User user = findUserByIdOrThrow(userId);
        user.updateUserIntroduction(requestDto.introduction());

        List<TechStack> newTechStacks = profileInfoUpdateMapper.toTechStacks(user, requestDto);
        user.updateTechStacks(newTechStacks);

        List<UserLink> newUserLinks = profileInfoUpdateMapper.toUserLinks(user, requestDto);
        user.updateUserLinks(newUserLinks);

        return profileInfoMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileInfoResponseDto getProfileByIdeaMemberId(Long ideaMemberId) {
        IdeaMember ideaMember = ideaMemberRepository.findByIdWithUser(ideaMemberId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.IDEA_MEMBER_NOT_FOUND.getMessage()));

        return profileInfoMapper.toDto(ideaMember.getUser());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechStackOptionsResponseDto> getAllTechStackOptions() {
        return Arrays.stream(TechStackType.values())
                .map(TechStackOptionsResponseDto::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserLinkOptionsResponseDto> getAllUserLinkOptions() {
        return Arrays.stream(LinkType.values())
                .map(UserLinkOptionsResponseDto::from)
                .toList();
    }

    @Override
    public List<MypageProjectGalleryResponseDto> getUserGalleryProjects(Long userId) {
        List<GalleryProjectMember> members = galleryProjectMemberRepository.findAllByUserId(userId);

        return members.stream()
                .sorted(Comparator.comparing(
                        (GalleryProjectMember m) -> m.getProject().getCreatedAt()
                ).reversed())
                .map(mypageProjectGalleryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public MypageProjectGalleryResponseDto updateProjectExhibit(Long userId, ProjectExhibitUpdateRequestDto requestDto) {
        GalleryProjectMember myMember = galleryProjectMemberRepository
                .findByUserIdAndProjectId(userId, requestDto.projectId())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.PROJECT_NOT_FOUND_OR_NOT_ASSOCIATED.getMessage()));

        if (myMember.getRole() != MemberRole.LEADER) {
            throw new AccessDeniedException(ExceptionMessage.ONLY_LEADER_CAN_UPDATE_PROJECT_EXHIBIT.getMessage());
        }

        GalleryProject project = myMember.getProject();
        project.updateExhibited(requestDto.exhibited());

        return mypageProjectGalleryMapper.toDto(myMember);
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.USER_NOT_EXIST.getMessage()));
    }

}
