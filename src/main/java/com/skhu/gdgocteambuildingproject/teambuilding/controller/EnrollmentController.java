package com.skhu.gdgocteambuildingproject.teambuilding.controller;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.ApplicantEnrollmentResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.EnrollmentAvailabilityResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final EnrollmentService enrollmentService;

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
    private ResponseEntity<EnrollmentAvailabilityResponseDto> getEnrollmentInfo(
            Principal principal,
            @PathVariable long ideaId
    ) {
        long userId = findUserIdBy(principal);

        EnrollmentAvailabilityResponseDto response = enrollmentService.getAvailabilityInfo(ideaId, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    @Operation(
            summary = "본인의 지원 내역(현황) 조회",
            description = """
                    본인이 다른 아이디어에 지원한 내역을 조회합니다.
                    
                    scheduleType: IDEA_REGISTRATION, FIRST_TEAM_BUILDING, FIRST_TEAM_BUILDING_ANNOUNCEMENT, SECOND_TEAM_BUILDING, SECOND_TEAM_BUILDING_ANNOUNCEMENT, THIRD_TEAM_BUILDING, FINAL_RESULT_ANNOUNCEMENT
                    지원 가능한 일정에 대해서만 동작하기 때문에, 실제로는 FIRST_TEAM_BUILDING, SECOND_TEAM_BUILDING만 요청할 수 있습니다.
                    
                    choice: FIRST, SECOND, THIRD
                    
                    enrollmentPart: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    """
    )
    private ResponseEntity<List<ApplicantEnrollmentResponseDto>> findApplyHistory(
            Principal principal,
            @RequestParam ScheduleType scheduleType
    ) {
        long userId = findUserIdBy(principal);

        List<ApplicantEnrollmentResponseDto> response = enrollmentService.getApplyHistory(userId, scheduleType);

        return ResponseEntity.ok(response);
    }


    private long findUserIdBy(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
