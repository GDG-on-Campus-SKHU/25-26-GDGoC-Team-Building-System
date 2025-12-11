package com.skhu.gdgocteambuildingproject.admin.api;

import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectCreateRequestDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectInfoPageResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ModifiableProjectResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.admin.dto.project.SchoolResponseDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.PastProjectResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "관리자 프로젝트 관리 API", description = "관리자용 프로젝트 관리 API입니다")
public interface AdminProjectManageApi {

    String DEFAULT_PAGE = "0";
    String DEFAULT_SIZE = "20";
    String DEFAULT_SORT_BY = "id";
    String DEFAULT_ORDER = "ASC";

    @Operation(
            summary = "새 프로젝트 등록",
            description = "새로운 프로젝트를 생성합니다."
    )
    ResponseEntity<Void> createNewProject(ProjectCreateRequestDto requestDto);

    @Operation(
            summary = "프로젝트 조회",
            description = """
                    모든 프로젝트를 조회합니다.
                    
                    sortBy(정렬 기준): id(순번), name(프로젝트명)
                    
                    order: ASC 또는 DESC
                    """
    )
    ResponseEntity<ProjectInfoPageResponseDto> getProjects(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") int page,
            @Parameter(description = "페이지 당 항목 수", example = "20") int size,
            @Parameter(description = "정렬 기준 필드명 (id, name)", example = "id") String sortBy,
            @Parameter(description = "정렬 순서 (ASC 또는 DESC)") SortOrder order
    );

    @Operation(
            summary = "지난 프로젝트 조회",
            description = "과거에 진행된 프로젝트 목록을 조회합니다."
    )
    ResponseEntity<List<PastProjectResponseDto>> getPastProjects();

    @Operation(
            summary = "수정 가능한 프로젝트 조회",
            description = """
                    아직 끝나지 않은 프로젝트 중 시작 일자가 가장 빠른 프로젝트를 조회합니다.
                    
                    이미 시작한 프로젝트더라도 아직 끝나지 않았으면 조회 대상에 포함됩니다.
                    
                    일정이 모두 결정된 프로젝트가 없다면, 아직 일정이 정해지지 않은 프로젝트를 조회합니다.
                    
                    해당하는 프로젝트가 존재하지 않는다면 404를 응답합니다.
                    
                    part: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    """
    )
    ResponseEntity<ModifiableProjectResponseDto> getModifiableProject();

    @Operation(
            summary = "프로젝트 정보 수정",
            description = """
                    프로젝트를 수정합니다.
                    
                    part: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    
                    scheduleType: IDEA_REGISTRATION, FIRST_TEAM_BUILDING, FIRST_TEAM_BUILDING_ANNOUNCEMENT, SECOND_TEAM_BUILDING, SECOND_TEAM_BUILDING_ANNOUNCEMENT, THIRD_TEAM_BUILDING, FINAL_RESULT_ANNOUNCEMENT
                    
                    topics: 프로젝트 토픽 목록 (문자열 리스트)
                    """
    )
    ResponseEntity<Void> updateProject(
            @Parameter(description = "프로젝트 ID", example = "1") long projectId,
            ProjectUpdateRequestDto requestDto
    );

    @Operation(
            summary = "학교 목록 조회",
            description = """
                    회원이 존재하는 모든 학교 목록을 조회합니다.
                    """
    )
    ResponseEntity<List<SchoolResponseDto>> getSchools();

    @Operation(
            summary = "프로젝트 삭제",
            description = """
                    프로젝트를 삭제합니다.
                    
                    별도 검증 로직을 수행하지 않고 바로 삭제하니 주의해서 호출해야 합니다.
                    """
    )
    ResponseEntity<Void> deleteProject(
            @Parameter(description = "프로젝트 ID", example = "1") long projectId
    );
}
