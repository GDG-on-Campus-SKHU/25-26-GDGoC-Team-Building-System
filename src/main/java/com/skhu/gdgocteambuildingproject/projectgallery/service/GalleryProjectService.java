package com.skhu.gdgocteambuildingproject.projectgallery.service;

import com.skhu.gdgocteambuildingproject.projectgallery.dto.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.GalleryProjectListResponseDto;

public interface GalleryProjectService {
    GalleryProjectInfoResponseDto findCurrentGalleryProjectInfoByProjectId(Long projectId);

    GalleryProjectListResponseDto findGalleryProjects(String generation);
}
