package com.skhu.gdgocteambuildingproject.admin.api;

import com.skhu.gdgocteambuildingproject.admin.dto.dashboard.DashboardSummaryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "관리자 대시보드 관리 API", description = "관리자용 대시보드 관리 API")
public interface AdminDashboardApi {
    @Operation(
            summary = "관리자 대시보드 요약 정보 조회",
            description = "유저 통계(승인 대기, 승인 완료)와 현재 진행 중인 모든 프로젝트의 핵심 요약 정보 리스트를 반환합니다. " +
                    "프로젝트 정보는 이름, 아이디어 수, 확정 인원/전체 인원," +
                    " 현재 진행 중인 일정 타입(Enum Name), 마감 기한 등의 데이터로 제공됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대시보드 정보 조회 성공")
    })
    ResponseEntity<DashboardSummaryResponseDto> getDashboard();
}