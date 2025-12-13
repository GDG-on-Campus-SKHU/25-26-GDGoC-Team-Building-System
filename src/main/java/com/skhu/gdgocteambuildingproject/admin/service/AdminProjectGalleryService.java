package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryResponseDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;

import java.util.List;

public interface AdminProjectGalleryService {
    List<ProjectGalleryResponseDto> searchProjectGallery(String keyword);

    List<ProjectGalleryResponseDto> getProjectGallerys(
            int page,
            int size,
            String sortBy,
            SortOrder order
    );
}
