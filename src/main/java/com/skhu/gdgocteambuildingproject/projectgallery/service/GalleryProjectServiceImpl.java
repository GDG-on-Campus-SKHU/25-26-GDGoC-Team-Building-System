package com.skhu.gdgocteambuildingproject.projectgallery.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_LIST_NOT_EXIST_IN_GALLERY;
import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST_IN_GALLERY;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.exception.GalleryProjectNotExistException;
import com.skhu.gdgocteambuildingproject.projectgallery.model.GalleryProjectInfoMapper;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.GalleryProjectListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectRepository;
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
    private final GalleryProjectInfoMapper galleryProjectInfoMapper;

    @Override
    @Transactional(readOnly = true)
    public GalleryProjectInfoResponseDto findCurrentGalleryProjectInfoByProjectId(Long projectId) {
        GalleryProject galleryProject = galleryProjectRepository.findById(projectId)
                .orElseThrow(() -> new GalleryProjectNotExistException(PROJECT_NOT_EXIST_IN_GALLERY.getMessage()));

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
}
