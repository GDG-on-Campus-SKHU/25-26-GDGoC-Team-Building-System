package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryDetailResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryResponseDto;
import com.skhu.gdgocteambuildingproject.admin.model.ProjectGalleryDetailMapper;
import com.skhu.gdgocteambuildingproject.admin.model.ProjectGalleryInfoMapper;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectRepository;
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
        GalleryProject galleryProject = galleryProjectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.PROJECT_NOT_EXIST_IN_GALLERY.getMessage()));

        return projectGalleryDetailMapper.toDto(galleryProject);
    }

    private Pageable createPageable(int page, int size, String sortBy, SortOrder order) {
        return PageRequest.of(page, size, order.sort(sortBy));
    }
}
