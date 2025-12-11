package com.skhu.gdgocteambuildingproject.teambuilding.controller;

import static com.skhu.gdgocteambuildingproject.global.util.PrincipalUtil.getUserIdFrom;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.ProjectParticipationAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.TeamBuildingInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
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
        name = "팀빌딩 프로젝트 API",
        description = "'SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS' 중 하나의 권한이 있어야 호출할 수 있습니다."
)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/availability")
    @Operation(
            summary = "팀빌딩 참여 가능 여부 조회",
            description = """
                    본인이 현재 진행중/예정된 프로젝트의 팀빌딩에 참여할 수 있는지 여부를 조회합니다.
                    """
    )
    public ResponseEntity<ProjectParticipationAvailabilityResponseDto> checkParticipationAvailability(
            Principal principal
    ) {
        long userId = getUserIdFrom(principal);

        ProjectParticipationAvailabilityResponseDto response = projectService.checkParticipationAvailability(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects/current")
    @Operation(
            summary = "프로젝트 정보 및 일정 조회",
            description = """
                    현재 진행중이거나 예정된 프로젝트의 정보 및 일정을 조회합니다.
                    또한, 본인이 해당 프로젝트에 아이디어를 등록할 수 있는지, 다른 아이디어에 지원할 수 있는지 여부를 같이 반환합니다.
                    
                    현재 진행중인 프로젝트가 없을 경우, 가장 최근에 예정된 프로젝트 정보를 반환합니다.
                    
                    예정된 프로젝트가 없을 경우 404 응답을 반환합니다.
                    
                    part: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    
                    scheduleType: IDEA_REGISTRATION, FIRST_TEAM_BUILDING, FIRST_TEAM_BUILDING_ANNOUNCEMENT, SECOND_TEAM_BUILDING, SECOND_TEAM_BUILDING_ANNOUNCEMENT, THIRD_TEAM_BUILDING, FINAL_RESULT_ANNOUNCEMENT
                    """
    )
    public ResponseEntity<TeamBuildingInfoResponseDto> findCurrentProjectInfo(
            Principal principal
    ) {
        long userId = getUserIdFrom(principal);

        TeamBuildingInfoResponseDto response = projectService.findCurrentProjectInfo(userId);

        return ResponseEntity.ok(response);
    }
}
