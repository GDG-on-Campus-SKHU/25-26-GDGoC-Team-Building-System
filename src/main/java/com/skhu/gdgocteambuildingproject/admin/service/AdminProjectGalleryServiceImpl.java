package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.GalleryProjectMemberUpdateDto;
import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryDetailResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.admin.model.ProjectGalleryDetailMapper;
import com.skhu.gdgocteambuildingproject.admin.model.ProjectGalleryInfoMapper;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectMemberRepository;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminProjectGalleryServiceImpl implements AdminProjectGalleryService {

    private final GalleryProjectRepository galleryProjectRepository;
    private final GalleryProjectMemberRepository galleryProjectMemberRepository;
    private final UserRepository userRepository;

    private final ProjectGalleryInfoMapper projectGalleryInfoMapper;
    private final ProjectGalleryDetailMapper projectGalleryDetailMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProjectGalleryResponseDto> searchProjectGallery(String keyword) {
        String searchKeyword = keyword.trim();

        List<GalleryProject> galleryProjects =
                galleryProjectRepository.findByProjectNameContainingAndExhibitedTrue(searchKeyword);

        return galleryProjects.stream()
                .map(projectGalleryInfoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectGalleryResponseDto> getProjectGallerys(int page, int size, String sortBy, SortOrder order) {
        Pageable pageable = createPageable(page, size, sortBy, order);

        Page<GalleryProject> galleryPage = galleryProjectRepository.findAll(pageable);

        return galleryPage.stream()
                .map(projectGalleryInfoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectGalleryDetailResponseDto getProjectGallery(Long projectId) {
        GalleryProject galleryProject = getGalleryProjectOrThrow(projectId);

        return projectGalleryDetailMapper.toDto(galleryProject);
    }

    @Override
    @Transactional
    public void updateProjectGallery(Long projectId, ProjectGalleryUpdateRequestDto dto) {
        GalleryProject galleryProject = getGalleryProjectOrThrow(projectId);
        User leader = getUserOrThrow(dto.leaderId());

        updateProjectInfo(galleryProject, dto, leader);

        replaceProjectMembers(galleryProject, leader, dto.leaderPart(), dto.members());
        galleryProject.updateExhibited(dto.exhibited());
    }

    private void updateProjectInfo(GalleryProject galleryProject, ProjectGalleryUpdateRequestDto dto, User leader) {
        galleryProject.updateByAdmin(
                dto.projectName(),
                Generation.fromLabel(dto.generation()),
                dto.shortDescription(),
                dto.serviceStatus(),
                dto.description(),
                leader
        );
    }

    private void replaceProjectMembers(GalleryProject galleryProject, User leader, String leaderPart,
                                       List<GalleryProjectMemberUpdateDto> members) {
        galleryProject.clearMembers();

        addLeader(galleryProject, leader, leaderPart);
        addMembers(galleryProject, members);
    }

    private void addMembers(GalleryProject project, List<GalleryProjectMemberUpdateDto> members) {
        if (members == null || members.isEmpty()) {
            return;
        }

        for (GalleryProjectMemberUpdateDto dto : members) {
            User user = getUserOrThrow(dto.userId());

            addMember(
                    project,
                    user,
                    Part.from(dto.part()),
                    MemberRole.MEMBER
            );
        }
    }

    private void addLeader(GalleryProject project, User leader, String leaderPart) {
        addMember(
                project,
                leader,
                Part.from(leaderPart),
                MemberRole.LEADER
        );
    }

    private void addMember(
            GalleryProject project,
            User user,
            Part part,
            MemberRole role
    ) {
        GalleryProjectMember member = GalleryProjectMember.builder()
                .role(role)
                .part(part)
                .user(user)
                .project(project)
                .build();

        galleryProjectMemberRepository.save(member);
        project.getMembers().add(member);
    }

    private Pageable createPageable(int page, int size, String sortBy, SortOrder order) {
        return PageRequest.of(page, size, order.sort(sortBy));
    }

    private GalleryProject getGalleryProjectOrThrow(Long projectId) {
        return galleryProjectRepository.findById(projectId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.PROJECT_NOT_EXIST_IN_GALLERY.getMessage()));
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND.getMessage()));
    }
}
