package com.skhu.gdgocteambuildingproject.projectgallery.service;

import com.skhu.gdgocteambuildingproject.projectgallery.dto.GalleryProjectInfoResponseDto;

public interface GalleryProjectService {
    GalleryProjectInfoResponseDto findCurrentGalleryProjectInfoByProjectId(Long projectId);
}
