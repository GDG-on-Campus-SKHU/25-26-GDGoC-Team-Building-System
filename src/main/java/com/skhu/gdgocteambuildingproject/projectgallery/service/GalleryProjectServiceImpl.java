package com.skhu.gdgocteambuildingproject.projectgallery.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_LIST_NOT_EXIST_IN_GALLERY;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST_IN_GALLERY;

import com.skhu.gdgocteambuildingproject.global.aws.domain.File;
import com.skhu.gdgocteambuildingproject.global.aws.repository.FileRepository;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectFile;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.MemberSearchListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.info.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.create.GalleryProjectSaveRequestDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.create.GalleryProjectMemberInfoDto;
import com.skhu.gdgocteambuildingproject.projectgallery.model.mapper.GalleryProjectInfoMapper;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.info.GalleryProjectListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.model.mapper.GalleryProjectMemberMapper;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectFileRepository;
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
import java.util.Map;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProjectServiceImpl implements GalleryProjectService {

    private final GalleryProjectRepository galleryProjectRepository;
    private final GalleryProjectMemberRepository galleryProjectMemberRepository;
    private final GalleryProjectInfoMapper galleryProjectInfoMapper;
    private final UserRepository userRepository;
    private final GalleryProjectMemberMapper galleryProjectMemberMapper;
    private final FileRepository fileRepository;
    private final GalleryProjectFileRepository galleryProjectFileRepository;

    @Override
    @Transactional
    public Long exhibitProject(GalleryProjectSaveRequestDto requestDto) {
        User leader = getUser(requestDto.leaderId());
        GalleryProject project = createGalleryProjectEntity(requestDto, leader);

        saveProjectMembers(project, requestDto.members());
        saveProjectFiles(project,requestDto.fileIds());
        return project.getId();
    }

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
    public MemberSearchListResponseDto searchMemberByName(String name) {
        List<User> users = userRepository.findByNameContaining(name);

        return galleryProjectMemberMapper.mapSearchMembers(users);
    }

    @Override
    public Long updateGalleryProjectByProjectId(Long projectId, GalleryProjectSaveRequestDto requestDto) {
        GalleryProject galleryProject = findGalleryProjectById(projectId);
        galleryProject.update(
                requestDto.projectName(),
                requestDto.generation(),
                requestDto.shortDescription(),
                requestDto.serviceStatus(),
                requestDto.description(),
                getUser(requestDto.leaderId())
        );
        saveProjectMembers(galleryProject, requestDto.members());
        saveProjectFiles(galleryProject, requestDto.fileIds());
        return projectId;
    }

    private GalleryProject findGalleryProjectById(Long projectId) {
        return galleryProjectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXIST_IN_GALLERY.getMessage()));
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
            throw new EntityNotFoundException(PROJECT_LIST_NOT_EXIST_IN_GALLERY.getMessage());
        }
    }

    private GalleryProject createGalleryProjectEntity(GalleryProjectSaveRequestDto requestDto, User leader) {
        return galleryProjectRepository.save(
                GalleryProject.builder()
                        .projectName(requestDto.projectName())
                        .generation(requestDto.generation())
                        .shortDescription(requestDto.shortDescription())
                        .serviceStatus(requestDto.serviceStatus())
                        .description(requestDto.description())
                        .user(leader)
                        .build()
        );
    }

    private void saveProjectMembers(GalleryProject project, List<GalleryProjectMemberInfoDto> members) {
        Long leaderId = project.getUser().getId();
        divideMemberAndSave(project, members, leaderId);
    }

    private void divideMemberAndSave(GalleryProject project, List<GalleryProjectMemberInfoDto> members, Long leaderId) {
        for (GalleryProjectMemberInfoDto memberDto : members) {
            User user = getUser(memberDto.userId());
            MemberRole role = resolveRole(leaderId, memberDto.userId());
            GalleryProjectMember member = GalleryProjectMember.builder()
                    .role(role)
                    .part(memberDto.part())
                    .user(user)
                    .project(project)
                    .build();

            galleryProjectMemberRepository.save(member);
            project.getMembers().add(member);
        }
    }

    private void saveProjectFiles(GalleryProject project, List<Long> fileIds) {
        for (Long fileId : fileIds) {
            File file = getFile(fileId);

            GalleryProjectFile galleryProjectFile = GalleryProjectFile.builder()
                    .file(file)
                    .project(project)
                    .build();

            galleryProjectFileRepository.save(galleryProjectFile);
            project.getFiles().add(galleryProjectFile);
        }
    }

    private User getUser(Long leaderId) {
        return userRepository.findById(leaderId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.USER_NOT_EXIST.getMessage()));
    }

    private File getFile(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.FILE_NOT_EXIST.getMessage()));
    }

    private MemberRole resolveRole(Long leaderId, Long memberId) {
        Map<Boolean, MemberRole> selector = Map.of(
                true, MemberRole.LEADER,
                false, MemberRole.MEMBER
        );

        return selector.get(memberId.equals(leaderId));
    }
}
