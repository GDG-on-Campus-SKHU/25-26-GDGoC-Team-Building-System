package com.skhu.gdgocteambuildingproject.teambuilding.controller;

import static com.skhu.gdgocteambuildingproject.global.util.PrincipalUtil.getUserIdFrom;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentDetermineRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentReadableResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.EnrollmentRequestDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.ReceivedEnrollmentsResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.enrollment.SentEnrollmentsResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.service.EnrollmentService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enrollments")
@PreAuthorize("hasAnyRole('SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS')")
@Tag(
        name = "팀빌딩 지원 API",
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
            @RequestBody @Valid EnrollmentRequestDto requestDto
    ) {
        long userId = getUserIdFrom(principal);

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
            @Valid @RequestBody EnrollmentDetermineRequestDto requestDto
    ) {
        long userId = getUserIdFrom(principal);

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
        long userId = getUserIdFrom(principal);

        EnrollmentAvailabilityResponseDto response = enrollmentService.getAvailabilityInfo(ideaId, userId);

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
    public ResponseEntity<SentEnrollmentsResponseDto> findSentEnrollments(
            Principal principal,
            @RequestParam ScheduleType scheduleType
    ) {
        long userId = getUserIdFrom(principal);

        SentEnrollmentsResponseDto response = enrollmentService.getSentEnrollments(userId, scheduleType);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sent/readabilities")
    @Operation(
            summary = "지원 내역 조회 가능 여부 조회",
            description = """
                    일정별로, 본인이 해당 일정의 지원 내역을 조회할 수 있는지 여부를 응답합니다.
                    
                    조회의 조건:
                     - 1차 팀빌딩: 1차 팀빌딩 기간이 시작한 이후라면 조회할 수 있다.
                     - 2차 팀빌딩: 2차 팀빌딩 기간이 시작한 이후면서, 1차 팀빌딩 때 멤버로 수락되지 않았다면 조회할 수 있다.
                    
                    scheduleType: FIRST_TEAM_BUILDING, SECOND_TEAM_BUILDING
                    """
    )
    public ResponseEntity<List<EnrollmentReadableResponseDto>> getSentEnrollmentReadabilities(
            Principal principal
    ) {
        long userId = getUserIdFrom(principal);

        List<EnrollmentReadableResponseDto> response = enrollmentService.getSentEnrollmentReadabilities(userId);

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
    public ResponseEntity<ReceivedEnrollmentsResponseDto> findReceivedApplyHistory(
            Principal principal,
            @RequestParam ScheduleType scheduleType
    ) {
        long userId = getUserIdFrom(principal);

        ReceivedEnrollmentsResponseDto response = enrollmentService.getReceivedEnrollments(userId, scheduleType);

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
        long userId = getUserIdFrom(principal);

        enrollmentService.cancelEnrollment(userId, enrollmentId);

        return NO_CONTENT;
    }
}
