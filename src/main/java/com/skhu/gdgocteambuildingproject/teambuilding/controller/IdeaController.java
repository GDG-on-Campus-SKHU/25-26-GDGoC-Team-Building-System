package com.skhu.gdgocteambuildingproject.teambuilding.controller;

import static com.skhu.gdgocteambuildingproject.global.util.PrincipalUtil.getUserIdFrom;

import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaCreateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaTextUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaDetailInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.IdeaTitleInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.idea.RosterResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.service.IdeaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS')")
@Tag(
        name = "팀빌딩 아이디어 API",
        description = "'SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS' 중 하나의 권한이 있어야 호출할 수 있습니다."
)
public class IdeaController {

    private static final ResponseEntity<Void> NO_CONTENT = ResponseEntity.noContent().build();

    private final IdeaService ideaService;

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
                    
                    topicId: 특정 주제(topic)를 사용하는 아이디어만 필터링 (선택적)
                    """
    )
    public ResponseEntity<IdeaTitleInfoPageResponseDto> findIdeas(
            Principal principal,
            @PathVariable long projectId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam SortOrder order,
            @RequestParam boolean recruitingOnly,
            @RequestParam(required = false) Long topicId
    ) {
        long userId = getUserIdFrom(principal);

        IdeaTitleInfoPageResponseDto response = ideaService.findIdeas(projectId, userId, page, size, sortBy, order, recruitingOnly, topicId);

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

    @GetMapping("/ideas/roster")
    @Operation(
            summary = "팀원 구성 조회",
            description = """
                    본인이 소속한 아이디어(팀)에 대한 정보와 팀원 구성 정보를 조회합니다.
                    
                    또한, 본인의 역할도 같이 반환합니다.
                    
                    소속한 아이디어가 없을 경우 404를 응답합니다.
                    
                    아이디어에 지원 가능한 파트와 별개로, 항상 모든 파트에 대한 정보를 조회합니다.
                    (지원 불가능한 파트의 경우, 현재 인원수와 최대 인원수가 0으로 설정됩니다)
                    
                    userId: 회원 정보에 대한 ID(프로필 조회 등에 사용 가능)
                    
                    part: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    
                    myRole, memberRole: CREATOR, MEMBER
                    
                    confirmed: 해당 멤버의 참여가 확정되었는지 여부(확정된 멤버는 삭제할 수 없음)
                    """
    )
    public ResponseEntity<RosterResponseDto> findMyIdeaComposition(
            Principal principal
    ) {
        long userId = getUserIdFrom(principal);

        RosterResponseDto response = ideaService.getComposition(userId);

        return ResponseEntity.ok(response);
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

    @DeleteMapping("/ideas/{ideaId}/members/{memberId}")
    @Operation(
            summary = "멤버 삭제",
            description = """
                    아이디어의 팀원을 제거합니다.
                    팀장(CREATOR)만 호출할 수 있습니다.
                    
                    이미 확정된(confirmed = true) 멤버라면 제거할 수 없습니다.
                    
                    memberId: 팀원의 userId
                    """
    )
    public ResponseEntity<Void> deleteMember(
            Principal principal,
            @PathVariable long ideaId,
            @PathVariable long memberId
    ) {
        long userId = getUserIdFrom(principal);

        ideaService.removeMember(userId, ideaId, memberId);

        return NO_CONTENT;
    }
}
