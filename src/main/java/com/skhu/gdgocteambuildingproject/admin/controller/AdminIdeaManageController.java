package com.skhu.gdgocteambuildingproject.admin.controller;

import com.skhu.gdgocteambuildingproject.admin.api.AdminIdeaManageApi;
import com.skhu.gdgocteambuildingproject.teambuilding.service.IdeaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/ideas")
@PreAuthorize("hasAnyRole('SKHU_ADMIN')")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminIdeaManageController implements AdminIdeaManageApi {

    private static final ResponseEntity<Void> NO_CONTENT = ResponseEntity.noContent().build();

    private final IdeaService ideaService;

    @Override
    @DeleteMapping("/{ideaId}")
    public ResponseEntity<Void> deleteIdea(@PathVariable long ideaId) {
        ideaService.hardDeleteIdea(ideaId);

        return NO_CONTENT;
    }
}
