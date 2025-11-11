package com.skhu.gdgocteambuildingproject.projectgallery.service;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.PROJECT_NOT_EXIST_IN_GALLERY;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.exception.GalleryProjectNotExistException;
import com.skhu.gdgocteambuildingproject.projectgallery.model.GalleryProjectInfoMapper;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProjectServiceImpl implements GalleryProjectService {

    private final GalleryProjectRepository galleryProjectRepository;
    private final GalleryProjectInfoMapper galleryProjectInfoMapper;

    @Override
    public GalleryProjectInfoResponseDto findCurrentGalleryProjectInfoByProjectId(Long projectId) {
        GalleryProject galleryProject = galleryProjectRepository.findById(projectId)
                .orElseThrow(() -> new GalleryProjectNotExistException(PROJECT_NOT_EXIST_IN_GALLERY.getMessage()));

        return galleryProjectInfoMapper.map(galleryProject);
    }
}
