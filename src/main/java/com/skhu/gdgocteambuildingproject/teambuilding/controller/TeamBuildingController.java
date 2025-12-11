package com.skhu.gdgocteambuildingproject.teambuilding.controller;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.IdeaTextUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.IdeaUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.IdeaCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaDetailInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaTitleInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.ProjectParticipationAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.TeamBuildingInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.service.IdeaService;
import com.skhu.gdgocteambuildingproject.teambuilding.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
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
@RequestMapping("/team-building")
@PreAuthorize("hasAnyRole('SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS')")
@Tag(
        name = "팀빌딩 API",
        description = "'SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS' 중 하나의 권한이 있어야 호출할 수 있습니다."
)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamBuildingController {

    private static final ResponseEntity<Void> NO_CONTENT = ResponseEntity.noContent().build();

    private final ProjectService projectService;
    private final IdeaService ideaService;

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

    @GetMapping("/projects")
    @Operation(
            summary = "프로젝트 정보 및 일정 조회",
            description = """
                    예정되었거나 현재 진행중인 프로젝트의 정보 및 일정을 조회합니다.
                    또한, 본인이 해당 프로젝트에 아이디어를 등록할 수 있는지 여부를 같이 반환합니다.
                    
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

    @PostMapping("/projects/{projectId}/ideas")
    @Operation(
            summary = "아이디어 생성",
            description = """
                    새로운 아이디어를 생성합니다.
                    registerStatus를 통해, 임시 저장할지 게시할지 지정할 수 있습니다.
                    
                    이미 게시한 아이디어가 있으면 예외를 던져 400을 응답합니다.
                    
                    임시 저장할 경우, 내부 값을 별도로 검증하지 않습니다.
                    
                    part: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    registerStatus: TEMPORARY, REGISTERED
                    """
    )
    public ResponseEntity<IdeaDetailInfoResponseDto> createIdea(
            Principal principal,
            @PathVariable long projectId,
            @RequestBody IdeaCreateRequestDto requestDto
    ) {
        long userId = getUserIdFrom(principal);

        IdeaDetailInfoResponseDto response = ideaService.createIdea(projectId, userId, requestDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects/{projectId}/ideas")
    @Operation(
            summary = "아이디어 목록 조회",
            description = """
                    프로젝트에 게시된 아이디어 목록을 조회합니다.
                    
                    sortBy(정렬 기준): id(순번), title(제목), introduction(한줄 소개), description(설명)
                    
                    order: ASC 또는 DESC
                    
                    recruitingOnly: 모집 중인 아이디어만 보기(인원이 최대로 차지 않은 아이디어만 보기)
                    """
    )
    public ResponseEntity<IdeaTitleInfoPageResponseDto> findIdeas(
            Principal principal,
            @PathVariable long projectId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam SortOrder order,
            @RequestParam boolean recruitingOnly
    ) {
        long userId = getUserIdFrom(principal);

        IdeaTitleInfoPageResponseDto response = ideaService.findIdeas(projectId, userId, page, size, sortBy, order, recruitingOnly);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects/{projectId}/ideas/{ideaId}")
    @Operation(
            summary = "아이디어 상세 조회",
            description = """
                    프로젝트에 게시된 아이디어 하나의 상세 정보를 조회합니다.
                    
                    part: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    """
    )
    public ResponseEntity<IdeaDetailInfoResponseDto> findIdeaDetails(
            Principal principal,
            @PathVariable long projectId,
            @PathVariable long ideaId
    ) {
        long userId = getUserIdFrom(principal);

        IdeaDetailInfoResponseDto response = ideaService.findIdeaDetail(projectId, ideaId, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects/{projectId}/ideas/temporary")
    @Operation(
            summary = "임시 저장된 아이디어 조회",
            description = """
                    본인이 임시 저장한 아이디어를 조회합니다.
                    
                    존재하지 않을 경우 404를 응답합니다.
                    """
    )
    public ResponseEntity<IdeaDetailInfoResponseDto> findTemporaryIdea(
            Principal principal,
            @PathVariable long projectId
    ) {
        long userId = getUserIdFrom(principal);

        IdeaDetailInfoResponseDto response = ideaService.findTemporaryIdea(projectId, userId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/projects/{projectId}/ideas/{ideaId}")
    @Operation(
            summary = "아이디어 수정(텍스트만)",
            description = """
                    아이디어의 내용(텍스트)을 수정합니다.
                    
                    일정에 상관 없이 호출할 수 있습니다.
                    """
    )
    public ResponseEntity<Void> updateIdea(
            Principal principal,
            @PathVariable long projectId,
            @PathVariable long ideaId,
            @RequestBody IdeaTextUpdateRequestDto requestDto
    ) {
        long userId = getUserIdFrom(principal);

        ideaService.updateTexts(projectId, ideaId, userId, requestDto);

        return NO_CONTENT;
    }

    @PutMapping("/projects/{projectId}/ideas/{ideaId}/before-enrollment")
    @Operation(
            summary = "아이디어 수정(모집 전)",
            description = """
                    아이디어의 내용 및 파트 구성 정보를 수정합니다.
                    
                    팀빌딩 시작 전에만 호출할 수 있습니다.(아이디어 등록 기간에만 호출할 수 있습니다)
                    
                    creatorPart, part: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    """
    )
    public ResponseEntity<Void> updateIdeaBeforeEnrollment(
            Principal principal,
            @PathVariable long projectId,
            @PathVariable long ideaId,
            @RequestBody IdeaUpdateRequestDto requestDto
    ) {
        long userId = getUserIdFrom(principal);

        ideaService.updateBeforeEnrollment(projectId, ideaId, userId, requestDto);

        return NO_CONTENT;
    }

    @DeleteMapping("/projects/{projectId}/ideas/{ideaId}")
    @Operation(
            summary = "아이디어 삭제",
            description = """
                    본인이 게시한 아이디어를 삭제합니다.
                    소프트 딜리트로 처리됩니다.
                    
                    이렇게 삭제된 아이디어는 관리자만 조회할 수 있습니다.
                    기존에 해당 아이디어에 지원한 내역은 모두 제거됩니다.
                    """
    )
    public ResponseEntity<Void> deleteIdea(
            Principal principal,
            @PathVariable long projectId,
            @PathVariable long ideaId
    ) {
        long userId = getUserIdFrom(principal);

        ideaService.softDeleteIdea(projectId, ideaId, userId);

        return NO_CONTENT;
    }

    private long getUserIdFrom(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
