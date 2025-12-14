package com.skhu.gdgocteambuildingproject.admin.controller;

import com.skhu.gdgocteambuildingproject.admin.api.AdminProjectGalleryApi;
import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryDetailResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.admin.service.AdminProjectGalleryService;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/gallery-project")
@PreAuthorize("hasAnyRole('SKHU_ADMIN')")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminProjectGalleryController implements AdminProjectGalleryApi {

    private final AdminProjectGalleryService adminProjectGalleryService;

    @Override
    @GetMapping("/search")
    public ResponseEntity<List<ProjectGalleryResponseDto>> searchProjectGallery(@RequestParam String keyword) {
        List<ProjectGalleryResponseDto> response = adminProjectGalleryService.searchProjectGallery(keyword);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ProjectGalleryResponseDto>> getProjectGallerys(
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_ORDER) SortOrder order
    ) {
        List<ProjectGalleryResponseDto> projectGallerys =
                adminProjectGalleryService.getProjectGallerys(page, size, sortBy, order);

        return ResponseEntity.ok(projectGallerys);
    }

    @Override
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectGalleryDetailResponseDto> getProjectGallery(@PathVariable Long projectId) {
        ProjectGalleryDetailResponseDto projectGallery = adminProjectGalleryService.getProjectGallery(projectId);
        return ResponseEntity.ok(projectGallery);
    }

    @Override
    @PatchMapping("/{projectId}")
    public ResponseEntity<Void> updateProjectGallery(@PathVariable Long projectId,
                                                     @RequestBody ProjectGalleryUpdateRequestDto dto) {
        adminProjectGalleryService.updateProjectGallery(projectId, dto);
        return ResponseEntity.ok().build();
    }
}
