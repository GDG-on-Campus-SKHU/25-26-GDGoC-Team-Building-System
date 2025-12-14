package com.skhu.gdgocteambuildingproject.projectgallery.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_LIST_NOT_EXIST_IN_GALLERY;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXHIBITED;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST_IN_GALLERY;

import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.util.PrincipalUtil;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.MemberSearchListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.TokenUserInfoForProjectBuildingResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.req.GalleryProjectMemberAddDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.req.GalleryProjectSaveRequestDto;
import com.skhu.gdgocteambuildingproject.projectgallery.model.mapper.GalleryProjectInfoMapper;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.model.mapper.GalleryProjectMemberMapper;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectMemberRepository;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.UserGeneration;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import com.skhu.gdgocteambuildingproject.user.repository.UserGenerationRepository;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProjectServiceImpl implements GalleryProjectService {

    private final GalleryProjectRepository galleryProjectRepository;
    private final GalleryProjectMemberRepository galleryProjectMemberRepository;
    private final GalleryProjectInfoMapper galleryProjectInfoMapper;
    private final UserRepository userRepository;
    private final GalleryProjectMemberMapper galleryProjectMemberMapper;
    private final UserGenerationRepository userGenerationRepository;

    @Override
    @Transactional
    public Long exhibitProject(GalleryProjectSaveRequestDto requestDto) {
        User leader = getUser(requestDto.leader().userId());
        GalleryProject project = createGalleryProjectEntity(requestDto, leader);

        saveProjectMembers(project, requestDto.leader(), requestDto.members());
        return project.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public GalleryProjectInfoResponseDto findCurrentGalleryProjectInfoByProjectId(Principal principal, Long projectId) {
        GalleryProject galleryProject = findGalleryProjectById(principal, projectId);

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
    public MemberSearchListResponseDto searchMemberByName(String name) {
        List<User> users = userRepository.findByNameContaining(name);

        return galleryProjectMemberMapper.mapSearchMembers(users);
    }

    @Override
    @Transactional
    public Long updateGalleryProjectByProjectId(
            Principal principal,
            Long projectId,
            GalleryProjectSaveRequestDto requestDto
    ) {
        GalleryProject galleryProject = findGalleryProjectById(principal, projectId);
        galleryProject.update(
                requestDto.projectName(),
                Generation.fromLabel(requestDto.generation()),
                requestDto.shortDescription(),
                requestDto.serviceStatus(),
                requestDto.description(),
                requestDto.thumbnailUrl(),
                getUser(requestDto.leader().userId())
        );
        updateProjectMembers(galleryProject, requestDto.leader(), requestDto.members());
        return projectId;
    }

    @Override
    public TokenUserInfoForProjectBuildingResponseDto findExhibitorInfo(Principal principal) {
        User user = getUser(PrincipalUtil.getUserIdFrom(principal));
        UserGeneration userGeneration = userGenerationRepository.findByUserAndIsMainTrue(user);

        return galleryProjectMemberMapper.mapExhibitor(user, userGeneration);
    }

//    @Override
//    @Transactional
//    public void deleteGalleryProjectByProjectId(Long projectId) {
//        GalleryProject galleryProject = findGalleryProjectById(projectId);
//        galleryProjectRepository.delete(galleryProject);
//    }

    private GalleryProject findGalleryProjectById(Principal principal, Long projectId) {
        GalleryProject galleryProject = galleryProjectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXIST_IN_GALLERY.getMessage()));
        User user = getUser(PrincipalUtil.getUserIdFrom(principal));

        if (!galleryProject.getExhibited() && !galleryProject.getUser().equals(user) && !user.getRole().equals(UserRole.ROLE_SKHU_ADMIN)) {
            throw new IllegalStateException(PROJECT_NOT_EXHIBITED.getMessage());
        }

        return galleryProject;
    }

    private GalleryProjectListResponseDto findAllGalleryProjects() {
        List<GalleryProject> galleryProjects =
                galleryProjectRepository.findByExhibitedTrueOrderByCreatedAtDesc();

        return mapToGalleryProjectsResponseDto(galleryProjects);
    }

    private GalleryProjectListResponseDto findGalleryProjectsByGeneration(String generation) {
        List<GalleryProject> galleryProjects =
                galleryProjectRepository.findByGenerationAndExhibitedTrueOrderByCreatedAtDesc(Generation.fromLabel(generation));

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
            throw new EntityNotFoundException(PROJECT_LIST_NOT_EXIST_IN_GALLERY.getMessage());
        }
    }

    private GalleryProject createGalleryProjectEntity(
            GalleryProjectSaveRequestDto requestDto,
            User leader
    ) {
        return galleryProjectRepository.save(
                GalleryProject.builder()
                        .projectName(requestDto.projectName())
                        .generation(Generation.fromLabel(requestDto.generation()))
                        .shortDescription(requestDto.shortDescription())
                        .serviceStatus(requestDto.serviceStatus())
                        .description(requestDto.description())
                        .thumbnailUrl(requestDto.thumbnailUrl())
                        .user(leader)
                        .build()
        );
    }

    private void saveProjectMembers(
            GalleryProject project,
            GalleryProjectMemberAddDto leader,
            List<GalleryProjectMemberAddDto> members
    ) {
        addLeaderWithProject(project, leader);
        addMembersWithProject(project, members);
    }

    private void addLeaderWithProject(
            GalleryProject project,
            GalleryProjectMemberAddDto leader
    ) {
        User user = getUser(leader.userId());
        GalleryProjectMember member = GalleryProjectMember.builder()
                .role(MemberRole.LEADER)
                .part(leader.part())
                .user(user)
                .project(project)
                .build();

        galleryProjectMemberRepository.save(member);
        project.getMembers().add(member);
    }

    private void addMembersWithProject(
            GalleryProject project,
            List<GalleryProjectMemberAddDto> members
    ) {
        if (members == null || members.isEmpty()) {
            return;
        }

        for (GalleryProjectMemberAddDto memberDto : members) {
            User user = getUser(memberDto.userId());
            GalleryProjectMember member = GalleryProjectMember.builder()
                    .role(MemberRole.MEMBER)
                    .part(memberDto.part())
                    .user(user)
                    .project(project)
                    .build();

            galleryProjectMemberRepository.save(member);
            project.getMembers().add(member);
        }
    }

    private void updateProjectMembers(
            GalleryProject project,
            GalleryProjectMemberAddDto leader,
            List<GalleryProjectMemberAddDto> members
    ) {
        project.clearMembers();
        addLeaderWithProject(project, leader);
        addMembersWithProject(project, members);
    }

    private User getUser(Long leaderId) {
        return userRepository.findById(leaderId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.USER_NOT_EXIST.getMessage()));
    }
}
