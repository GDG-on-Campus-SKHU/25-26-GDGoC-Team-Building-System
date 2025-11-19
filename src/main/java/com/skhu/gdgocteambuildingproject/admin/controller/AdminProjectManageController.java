package com.skhu.gdgocteambuildingproject.admin.controller;

import com.skhu.gdgocteambuildingproject.admin.api.AdminProjectManageApi;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.PastProjectResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.service.ProjectService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @GetMapping("/past")
    public ResponseEntity<List<PastProjectResponseDto>> getPastProjects() {
        List<PastProjectResponseDto> response = projectService.findPastProjects();

        return ResponseEntity.ok(response);
    }
}
