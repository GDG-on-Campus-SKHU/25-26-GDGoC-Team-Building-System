package com.skhu.gdgocteambuildingproject.teambuilding.controller;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.EnrollmentDetermineRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.EnrollmentRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.EnrollmentAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.ReceivedEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.SentEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.RosterResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enrollments")
@PreAuthorize("hasAnyRole('SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS')")
@Tag(
        name = "지원 API",
        description = "'SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS' 중 하나의 권한이 있어야 호출할 수 있습니다."
)
public class EnrollmentController {

    private static final ResponseEntity<Void> NO_CONTENT = ResponseEntity.noContent().build();

    private final EnrollmentService enrollmentService;

    @PostMapping("/ideas/{ideaId}")
    @Operation(
            summary = "지원",
            description = """
                    다른 사람이 게시한 아이디어에 지원합니다.
                    
                    이미 다른 아이디어에 소속된 상태(아이디어를 게시한 경우 포함)라면 지원할 수 없습니다.
                    
                    이미 해당 아이디어에 지원했거나, 이미 사용한 지망을 중복 사용시 4XX로 응답합니다.
                    """
    )
    public ResponseEntity<Void> enroll(
            Principal principal,
            @PathVariable long ideaId,
            @RequestBody EnrollmentRequestDto requestDto
    ) {
        long userId = findUserIdBy(principal);

        enrollmentService.enroll(userId, ideaId, requestDto);

        return NO_CONTENT;
    }

    @PostMapping("/{enrollmentId}/determine")
    @Operation(
            summary = "지원 수락/거절",
            description = """
                    지원을 수락하거나 거절합니다.
                    
                    accept의 값이 true면 수락, false면 거절합니다.
                    """
    )
    public ResponseEntity<Void> determine(
            Principal principal,
            @PathVariable long enrollmentId,
            @RequestBody EnrollmentDetermineRequestDto requestDto
    ) {
        long userId = findUserIdBy(principal);

        enrollmentService.determineEnrollment(userId, enrollmentId, requestDto);

        return NO_CONTENT;
    }

    @GetMapping("/availability/ideas/{ideaId}")
    @Operation(
            summary = "지원하기 정보 조회",
            description = """
                    '지원하기' 페이지를 렌더링할 때 필요한 정보를 조회합니다.
                    
                    현재 지원 가능한 일정이 아닐 경우 4XX 응답을 반환합니다.
                    
                    현재 진행 중인 프로젝트 일정 정보, 지원한 아이디어에 대한 정보, 각 지망 사용 가능 여부, 지원 가능 파트 정보를 반환합니다.
                    
                    scheduleType: IDEA_REGISTRATION, FIRST_TEAM_BUILDING, FIRST_TEAM_BUILDING_ANNOUNCEMENT, SECOND_TEAM_BUILDING, SECOND_TEAM_BUILDING_ANNOUNCEMENT, THIRD_TEAM_BUILDING, FINAL_RESULT_ANNOUNCEMENT
                    지원 가능한 일정에 대해서만 동작하기 때문에, 실제로는 FIRST_TEAM_BUILDING, SECOND_TEAM_BUILDING만 반환할 것입니다.
                    
                    choice: FIRST, SECOND, THIRD
                    
                    part: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    """
    )
    public ResponseEntity<EnrollmentAvailabilityResponseDto> getEnrollmentInfo(
            Principal principal,
            @PathVariable long ideaId
    ) {
        long userId = findUserIdBy(principal);

        EnrollmentAvailabilityResponseDto response = enrollmentService.getAvailabilityInfo(ideaId, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/roster")
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
        long userId = findUserIdBy(principal);

        RosterResponseDto response = enrollmentService.getComposition(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sent")
    @Operation(
            summary = "본인의 지원 내역(현황) 조회",
            description = """
                    본인이 다른 아이디어에 지원한 내역을 조회합니다.
                    
                    scheduleType: IDEA_REGISTRATION, FIRST_TEAM_BUILDING, FIRST_TEAM_BUILDING_ANNOUNCEMENT, SECOND_TEAM_BUILDING, SECOND_TEAM_BUILDING_ANNOUNCEMENT, THIRD_TEAM_BUILDING, FINAL_RESULT_ANNOUNCEMENT
                    지원 가능한 일정에 대해서만 동작하기 때문에, 실제로는 FIRST_TEAM_BUILDING, SECOND_TEAM_BUILDING만 요청할 수 있습니다.
                    
                    choice: FIRST, SECOND, THIRD
                    
                    enrollmentStatus: WAITING(수락 예정/거절 예정 상태도 WAITING으로 표시), EXPIRED, REJECTED, ACCEPTED
                    
                    enrollmentPart: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    
                    scheduleEnded: 일정(파라미터로 전달한 일정)이 마감되었는지 여부
                    """
    )
    public ResponseEntity<List<SentEnrollmentResponseDto>> findSentEnrollments(
            Principal principal,
            @RequestParam ScheduleType scheduleType
    ) {
        long userId = findUserIdBy(principal);

        List<SentEnrollmentResponseDto> response = enrollmentService.getSentEnrollments(userId, scheduleType);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/received")
    @Operation(
            summary = "받은 지원 내역(현황) 조회",
            description = """
                    본인이 게시한 아이디어에 다른 회원이 지원한 내역을 조회합니다.
                    
                    enrollmentAcceptable: 해당 지원을 수락할 수 있는지 여부입니다. 이미 해당 파트의 인원 수가 다 찼다면 false가 됩니다.
                    
                    scheduleType: IDEA_REGISTRATION, FIRST_TEAM_BUILDING, FIRST_TEAM_BUILDING_ANNOUNCEMENT, SECOND_TEAM_BUILDING, SECOND_TEAM_BUILDING_ANNOUNCEMENT, THIRD_TEAM_BUILDING, FINAL_RESULT_ANNOUNCEMENT
                    지원 가능한 일정에 대해서만 동작하기 때문에, 실제로는 FIRST_TEAM_BUILDING, SECOND_TEAM_BUILDING만 요청할 수 있습니다.
                    
                    choice: FIRST, SECOND, THIRD
                    
                    enrollmentStatus: WAITING, EXPIRED, REJECTED, ACCEPTED
                    
                    enrollmentPart: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    """
    )
    public ResponseEntity<List<ReceivedEnrollmentResponseDto>> findReceivedApplyHistory(
            Principal principal,
            @RequestParam ScheduleType scheduleType
    ) {
        long userId = findUserIdBy(principal);

        List<ReceivedEnrollmentResponseDto> response = enrollmentService.getReceivedEnrollments(userId, scheduleType);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{enrollmentId}")
    @Operation(
            summary = "지원 취소",
            description = """
                    지원을 취소합니다.
                    
                    '대기 중'(혹은 '수락 예정', '거절 예정')인 지원이 아니라면 400을 응답합니다.
                    """
    )
    public ResponseEntity<Void> cancelEnrollment(
            Principal principal,
            @PathVariable long enrollmentId
    ) {
        long userId = findUserIdBy(principal);

        enrollmentService.cancelEnrollment(userId, enrollmentId);

        return NO_CONTENT;
    }

    private long findUserIdBy(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
