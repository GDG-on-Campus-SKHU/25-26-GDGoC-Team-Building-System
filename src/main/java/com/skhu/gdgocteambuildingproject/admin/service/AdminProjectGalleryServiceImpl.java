package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryResponseDto;
import com.skhu.gdgocteambuildingproject.admin.model.ProjectGalleryInfoMapper;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminProjectGalleryServiceImpl implements AdminProjectGalleryService {

    private final GalleryProjectRepository galleryProjectRepository;

    private final ProjectGalleryInfoMapper projectGalleryInfoMapper;

    @Override
    @Transactional
    public List<ProjectGalleryResponseDto> searchProjectGallery(String keyword) {
        List<GalleryProject> galleryProjects = galleryProjectRepository.findByProjectName(keyword.trim());

        return galleryProjects.stream()
                .map(projectGalleryInfoMapper::toDto)
                .toList();
    }
}
