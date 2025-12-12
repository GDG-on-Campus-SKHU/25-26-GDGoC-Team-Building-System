package com.skhu.gdgocteambuildingproject.projectgallery.controller;

import com.skhu.gdgocteambuildingproject.projectgallery.api.GalleryProjectControllerApi;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.MemberSearchListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.req.GalleryProjectSaveRequestDto;
import com.skhu.gdgocteambuildingproject.projectgallery.service.GalleryProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project-gallery")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProjectController implements GalleryProjectControllerApi {

    private final GalleryProjectService galleryProjectService;

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('SKHU_ADMIN', 'SKHU_MEMBER')")
    public ResponseEntity<Long> exhibitProject(@RequestBody GalleryProjectSaveRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(galleryProjectService.exhibitProject(requestDto));
    }

    @Override
    @GetMapping("/{projectId}")
    public ResponseEntity<GalleryProjectInfoResponseDto> findCurrentGalleryProjectInfoByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(galleryProjectService.findCurrentGalleryProjectInfoByProjectId(projectId));
    }

    @Override
    @GetMapping("/projects")
    public ResponseEntity<GalleryProjectListResponseDto> findGalleryProjects(@RequestParam(defaultValue = "") String generation) {
        return ResponseEntity.ok(galleryProjectService.findGalleryProjects(generation));
    }

    @Override
    @GetMapping("/members/search")
    public ResponseEntity<MemberSearchListResponseDto> searchMemberList(@RequestParam String name) {
        return ResponseEntity.ok(galleryProjectService.searchMemberByName(name));
    }

    @Override
    @PutMapping("/{projectId}")
    @PreAuthorize("@galleryProjectAccessChecker.checkLeaderOrAdminPermission(#projectId, authentication)")
    public ResponseEntity<Long> updateProject(
            @PathVariable Long projectId,
            @RequestBody GalleryProjectSaveRequestDto requestDto
    ) {
        return ResponseEntity.ok(galleryProjectService.updateGalleryProjectByProjectId(projectId, requestDto));
    }

//    @Override
//    @DeleteMapping("/{projectId}")
//    @PreAuthorize("@galleryProjectAccessChecker.checkLeaderOrAdminPermission(#projectId, authentication)")
//    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
//        galleryProjectService.deleteGalleryProjectByProjectId(projectId);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
}
