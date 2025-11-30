package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryResponseDto;

import java.util.List;

public interface AdminProjectGalleryService {
    List<ProjectGalleryResponseDto> searchProjectGallery(String keyword);
}
