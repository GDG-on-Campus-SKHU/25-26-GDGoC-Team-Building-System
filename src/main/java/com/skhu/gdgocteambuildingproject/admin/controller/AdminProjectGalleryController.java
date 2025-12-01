package com.skhu.gdgocteambuildingproject.admin.controller;

import com.skhu.gdgocteambuildingproject.admin.api.AdminProjectGalleryApi;
import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryResponseDto;
import com.skhu.gdgocteambuildingproject.admin.service.AdminProjectGalleryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/gallery-project")
@PreAuthorize("hasAnyRole('SKHU_ADMIN')")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminProjectGalleryController implements AdminProjectGalleryApi {

    private final AdminProjectGalleryService adminProjectGalleryService;

    @GetMapping
    public ResponseEntity<List<ProjectGalleryResponseDto>> searchProjectGallery(@RequestParam String keyword) {
        List<ProjectGalleryResponseDto> response = adminProjectGalleryService.searchProjectGallery(keyword);
        return ResponseEntity.ok(response);
    }
}
