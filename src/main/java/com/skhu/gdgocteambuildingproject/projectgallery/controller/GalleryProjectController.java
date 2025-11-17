package com.skhu.gdgocteambuildingproject.projectgallery.controller;

import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.MemberSearchListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.GalleryProjectListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.service.GalleryProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/projects")
    @Operation(
            summary = "갤러리에 있는 프로젝트 목록 조회",
            description =
                    """
                    프로젝트 갤러리에 전시되어 있는 프로젝트 목록을 조회합니다. 정렬은 최신순으로 나열합니다.
                    
                    generation(기수)를 parameter로 필터링하고, parameter를 넣지 않으면 필터링 없이 모든 내용을 조회합니다.
                    
                    해당하는 프로젝트가 없을 경우, 404 응답을 반환합니다.
                    """
    )
    private ResponseEntity<GalleryProjectListResponseDto> findGalleryProjects(@RequestParam(defaultValue = "") String generation) {
        return ResponseEntity.ok(galleryProjectService.findGalleryProjects(generation));
    }

    @GetMapping("/{projectId}/members/search")
    @Operation(
            summary = "갤러리 생성시 팀원 등록을 위한 유저 목록 조회",
            description =
                    """
                    프로젝트 갤러리에서 팀원을 등록하기 위해 모든 유저를 검색합니다.
                    
                    프로젝트 갤러리에서 해당 프로젝트의 id로 유저가 해당 팀의 팀원인지 판독합니다.        
                    """
    )
    private ResponseEntity<MemberSearchListResponseDto> searchMemberList(@RequestParam String name, @PathVariable Long projectId) {
        return ResponseEntity.ok(galleryProjectService.searchMemberByName(projectId, name));
    }

    @PostMapping("/{projectId}/members/select")
    @Operation(
            summary = "유저 목록 조회 창에서 팀원으로 등록할 멤버 선택",
            description =
                    """
                    프로젝트 갤러리의 팀원 등록을 위한 유저 목록 조회 창에서 유저를 선택합니다.
                    
                    이미 등록된 유저는 등록하려고 할 경우, 400 응답을 반환합니다.
                    
                    또한, 해당하는 프로젝트가 없을 경우, 404 응답을 반환합니다.
                    """
    )
    private ResponseEntity<Void> selectMember(@RequestParam Long userId, @PathVariable Long projectId) {
        galleryProjectService.addMemberToProject(projectId, userId);
        return ResponseEntity.ok().build();
    }
}
