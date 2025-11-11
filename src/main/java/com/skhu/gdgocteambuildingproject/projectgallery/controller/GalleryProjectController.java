package com.skhu.gdgocteambuildingproject.projectgallery.controller;

import com.skhu.gdgocteambuildingproject.projectgallery.dto.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.service.GalleryProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project-gallery")
@Tag(
        name = "프로젝트 갤러리 API",
        description = "조회는 모든 사용자가 호출할 수 있지만, 생성은 'SKHU_ADMIN', 'SKHU_MEMBER' 중 하나의 권한이 있어야 호출할 수 있습니다."
)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProjectController {

    private final GalleryProjectService galleryProjectService;
    
    @GetMapping("/{projectId}")
    @Operation(
            summary = "갤러리에 있는 프로젝트 상세 조회",
            description = "프로젝트 갤러리에 전시되어 있는 프로젝트의 상세 정보를 조회합니다. id에 해당하는 프로젝트가 없을 경우, 404 응답을 반환합니다."
    )
    private ResponseEntity<GalleryProjectInfoResponseDto> findCurrentGalleryProjectInfoByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(galleryProjectService.findCurrentGalleryProjectInfoByProjectId(projectId));
    }
}
