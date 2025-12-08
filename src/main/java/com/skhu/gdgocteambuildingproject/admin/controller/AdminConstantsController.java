package com.skhu.gdgocteambuildingproject.admin.controller;

import com.skhu.gdgocteambuildingproject.admin.api.AdminConstantsApi;
import com.skhu.gdgocteambuildingproject.admin.dto.project.GenerationResponseDto;
import com.skhu.gdgocteambuildingproject.admin.service.AdminConstantsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/constants")
@PreAuthorize("hasAnyRole('SKHU_ADMIN')")
public class AdminConstantsController implements AdminConstantsApi {

    private final AdminConstantsService constantsService;

    @Override
    @GetMapping("/generations")
    public ResponseEntity<List<GenerationResponseDto>> getGenerations() {
        List<GenerationResponseDto> response = constantsService.getGenerations();

        return ResponseEntity.ok(response);
    }
}
