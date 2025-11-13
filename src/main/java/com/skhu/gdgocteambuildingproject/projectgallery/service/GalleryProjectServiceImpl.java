package com.skhu.gdgocteambuildingproject.projectgallery.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_LIST_NOT_EXIST_IN_GALLERY;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST_IN_GALLERY;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.MemberSearchListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.exception.GalleryProjectNotExistException;
import com.skhu.gdgocteambuildingproject.projectgallery.model.GalleryProjectInfoMapper;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.GalleryProjectListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.model.GalleryProjectMemberMapper;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectMemberRepository;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProjectServiceImpl implements GalleryProjectService {

    private final GalleryProjectRepository galleryProjectRepository;
    private final GalleryProjectMemberRepository galleryProjectMemberRepository;
    private final GalleryProjectInfoMapper galleryProjectInfoMapper;
    private final UserRepository userRepository;
    private final GalleryProjectMemberMapper galleryProjectMemberMapper;

    @Override
    @Transactional(readOnly = true)
    public GalleryProjectInfoResponseDto findCurrentGalleryProjectInfoByProjectId(Long projectId) {
        GalleryProject galleryProject = findGalleryProjectById(projectId);

        return galleryProjectInfoMapper.mapToInfo(galleryProject);
    }

    @Override
    @Transactional(readOnly = true)
    public GalleryProjectListResponseDto findGalleryProjects(String generation) {
        if (generation == null || generation.isEmpty()) {
            return findAllGalleryProjects();
        }

        return findGalleryProjectsByGeneration(generation);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberSearchListResponseDto searchMemberByName(Long projectId, String name) {
        List<User> users = userRepository.findByNameContaining(name);
        List<Long> selectedUserIds = getSelectedMemberIds(projectId);

        return galleryProjectMemberMapper.mapSearchMembers(users, selectedUserIds);
    }

    @Override
    @Transactional
    public void addMemberToProject(Long projectId, Long userId) {
        GalleryProject galleryProject = findGalleryProjectById(projectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.USER_NOT_EXIST.getMessage())); // 이후 UserService 코드에 작성해야 함!
        checkUserAlreadyJoinProject(projectId, userId);

        GalleryProjectMember member = GalleryProjectMember.builder()
                .role(MemberRole.MEMBER)
                .part(Part.NULL)
                .user(user)
                .project(galleryProject)
                .build();

        galleryProjectMemberRepository.save(member);
        galleryProject.getMembers().add(member);
    }

    private GalleryProject findGalleryProjectById(Long projectId) {
        return galleryProjectRepository.findById(projectId)
                .orElseThrow(() -> new GalleryProjectNotExistException(PROJECT_NOT_EXIST_IN_GALLERY.getMessage()));
    }

    private GalleryProjectListResponseDto findAllGalleryProjects() {
        List<GalleryProject> galleryProjects =
                galleryProjectRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        return mapToGalleryProjectsResponseDto(galleryProjects);
    }

    private GalleryProjectListResponseDto findGalleryProjectsByGeneration(String generation) {
        List<GalleryProject> galleryProjects =
                galleryProjectRepository.findByGenerationOrderByCreatedAtDesc(generation);

        return mapToGalleryProjectsResponseDto(galleryProjects);
    }

    private GalleryProjectListResponseDto mapToGalleryProjectsResponseDto(List<GalleryProject> galleryProjects) {
        throwIfGalleryProjectListEmpty(galleryProjects);

        return galleryProjectInfoMapper.mapToListDto(
                galleryProjects.stream()
                        .map(galleryProjectInfoMapper::mapToSummary)
                        .toList()
        );
    }

    private void throwIfGalleryProjectListEmpty(List<GalleryProject> galleryProjectList) {
        if (galleryProjectList.isEmpty()) {
            throw new GalleryProjectNotExistException(PROJECT_LIST_NOT_EXIST_IN_GALLERY.getMessage());
        }
    }

    private List<Long> getSelectedMemberIds(Long projectId) {
        return galleryProjectMemberRepository.findByProjectId(projectId).stream()
                .map(m -> m.getUser().getId())
                .toList();
    }

    private void checkUserAlreadyJoinProject(Long projectId, Long userId) {
        boolean exists = galleryProjectMemberRepository.existsByProjectIdAndUserId(projectId, userId);

        if (exists) {
            throw new IllegalArgumentException(ExceptionMessage.MEMBER_ALREADY_ON_TEAM.getMessage());
        }
    }
}
