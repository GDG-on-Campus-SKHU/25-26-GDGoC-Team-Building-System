package com.skhu.gdgocteambuildingproject.admin.controller;

import com.skhu.gdgocteambuildingproject.admin.api.AdminIdeaManageApi;
import com.skhu.gdgocteambuildingproject.admin.dto.idea.AdminIdeaDetailResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.idea.IdeaTitleInfoIncludeDeletedPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.IdeaUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.service.IdeaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('SKHU_ADMIN')")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminIdeaManageController implements AdminIdeaManageApi {

    private static final ResponseEntity<Void> NO_CONTENT = ResponseEntity.noContent().build();

    private final IdeaService ideaService;

    @Override
    @DeleteMapping("/ideas/{ideaId}")
    public ResponseEntity<Void> deleteIdea(@PathVariable long ideaId) {
        ideaService.hardDeleteIdea(ideaId);

        return NO_CONTENT;
    }

    @Override
    @GetMapping("/projects/{projectId}/ideas")
    public ResponseEntity<IdeaTitleInfoIncludeDeletedPageResponseDto> findIdeas(
            @PathVariable long projectId,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_ORDER) SortOrder order
    ) {
        IdeaTitleInfoIncludeDeletedPageResponseDto response = ideaService.findIdeasIncludeDeleted(
                projectId,
                page,
                size,
                sortBy,
                order
        );

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/projects/{projectId}/ideas/{ideaId}")
    public ResponseEntity<AdminIdeaDetailResponseDto> findIdeaDetail(
            @PathVariable long projectId,
            @PathVariable long ideaId
    ) {
        AdminIdeaDetailResponseDto response = ideaService.findIdeaDetailByAdmin(
                projectId,
                ideaId
        );

        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/ideas/{ideaId}")
    public ResponseEntity<Void> updateIdea(
            @PathVariable long ideaId,
            @RequestBody IdeaUpdateRequestDto requestDto
    ) {
        ideaService.updateIdeaByAdmin(ideaId, requestDto);

        return NO_CONTENT;
    }

    @Override
    @PostMapping("/ideas/{ideaId}/restore")
    public ResponseEntity<Void> restoreIdea(
            @PathVariable long ideaId
    ) {
        ideaService.restoreIdea(ideaId);

        return NO_CONTENT;
    }

    @Override
    @DeleteMapping("/ideas/{ideaId}/members/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable long ideaId,
            @PathVariable long memberId
    ) {
        ideaService.removeMemberByAdmin(ideaId, memberId);

        return NO_CONTENT;
    }
}
