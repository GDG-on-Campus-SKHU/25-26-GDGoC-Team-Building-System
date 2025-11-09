package com.skhu.gdgocteambuildingproject.teambuilding.controller;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.TeamBuildingInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.service.TeamBuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team-building")
@PreAuthorize("hasAnyRole('SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS')")
@Tag(
        name = "팀빌딩 API",
        description = "'SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS' 중 하나의 권한이 있어야 호출할 수 있습니다."
)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamBuildingController {
    private final TeamBuildingService teamBuildingService;

    @GetMapping
    @Operation(
            summary = "프로젝트 정보 및 일정 조회",
            description = "예정되었거나 현재 진행중인 프로젝트의 정보 및 일정을 조회합니다. 현재 진행중인 프로젝트가 없을 경우, 가장 최근에 예정된 프로젝트 정보를 반환합니다. 예정된 프로젝트가 없을 경우 404 응답을 반환합니다."
    )
    private ResponseEntity<TeamBuildingInfoResponseDto> findCurrentProjectInfo() {
        TeamBuildingInfoResponseDto responseDto = teamBuildingService.findCurrentProjectInfo();

        return ResponseEntity.ok(responseDto);
    }
}
