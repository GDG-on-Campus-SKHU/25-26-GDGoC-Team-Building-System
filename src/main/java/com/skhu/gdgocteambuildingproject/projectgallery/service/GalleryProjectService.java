package com.skhu.gdgocteambuildingproject.projectgallery.service;

import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.GalleryProjectListResponseDto;

public interface GalleryProjectService {
    GalleryProjectInfoResponseDto findCurrentGalleryProjectInfoByProjectId(Long projectId);

    GalleryProjectListResponseDto findGalleryProjects(String generation);
}
