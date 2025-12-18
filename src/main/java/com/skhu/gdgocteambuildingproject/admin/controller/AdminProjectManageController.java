package com.skhu.gdgocteambuildingproject.admin.controller;

import com.skhu.gdgocteambuildingproject.admin.api.AdminProjectManageApi;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ModifiableProjectResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectNameUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.SchoolResponseDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.PastProjectResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.service.ProjectService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/projects")
@PreAuthorize("hasAnyRole('SKHU_ADMIN')")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminProjectManageController implements AdminProjectManageApi {

    private static final ResponseEntity<Void> NO_CONTENT = ResponseEntity.noContent().build();

    private final ProjectService projectService;

    @Override
    @PostMapping
    public ResponseEntity<Void> createNewProject(
            @Valid @RequestBody ProjectCreateRequestDto requestDto
    ) {
        projectService.createNewProject(requestDto);

        return NO_CONTENT;
    }

    @Override
    @GetMapping
    public ResponseEntity<ProjectInfoPageResponseDto> getProjects(
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_ORDER) SortOrder order
    ) {
        ProjectInfoPageResponseDto response = projectService.findProjects(page, size, sortBy, order);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/past")
    public ResponseEntity<List<PastProjectResponseDto>> getPastProjects() {
        List<PastProjectResponseDto> response = projectService.findPastProjects();

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/modifiable")
    public ResponseEntity<ModifiableProjectResponseDto> getModifiableProject() {
        ModifiableProjectResponseDto response = projectService.findModifiableProject();

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{projectId}")
    public ResponseEntity<ModifiableProjectResponseDto> getProjectDetail(
            @PathVariable long projectId
    ) {
        ModifiableProjectResponseDto response = projectService.findProjectDetailById(projectId);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/schools")
    public ResponseEntity<List<SchoolResponseDto>> getSchools() {
        List<SchoolResponseDto> response = projectService.findSchools();

        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/{projectId}")
    public ResponseEntity<Void> updateProject(
            @PathVariable long projectId,
            @Valid @RequestBody ProjectUpdateRequestDto requestDto
    ) {
        projectService.updateProject(projectId, requestDto);

        return NO_CONTENT;
    }

    @Override
    @PutMapping("/{projectId}/name")
    public ResponseEntity<Void> updateProjectName(
            @PathVariable long projectId,
            @Valid @RequestBody ProjectNameUpdateRequestDto requestDto
    ) {
        projectService.updateProjectName(projectId, requestDto);

        return NO_CONTENT;
    }

    @Override
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable long projectId
    ) {
        projectService.deleteProject(projectId);

        return NO_CONTENT;
    }
}
