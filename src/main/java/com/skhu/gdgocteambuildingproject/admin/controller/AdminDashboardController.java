package com.skhu.gdgocteambuildingproject.admin.controller;

import com.skhu.gdgocteambuildingproject.admin.service.AdminDashboardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasAnyRole('SKHU_ADMIN')")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping
    public ResponseEntity<?> getDashboard() {
        adminDashboardService.getDashboard();
    }
}
