package com.skhu.gdgocteambuildingproject.admin.api;

import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryDetailResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "관리자 프로젝트 갤러리 관리 API", description = "관리자용 프로젝트 갤러리 관리 API")
public interface AdminProjectGalleryApi {

    String DEFAULT_PAGE = "0";
    String DEFAULT_SIZE = "20";
    String DEFAULT_SORT_BY = "id";
    String DEFAULT_ORDER = "ASC";

    @Operation(summary = "프로젝트 갤러리에서 프로젝트명으로 검색", description = "프로젝트 갤러리에서 프로젝트명으로 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    ResponseEntity<List<ProjectGalleryResponseDto>> searchProjectGallery(
            @Parameter(description = "프로젝트 명 검색어") String keyword
    );

    @Operation(summary = "프로젝트 갤러리 목록 조회", description = "전체 프로젝트 갤러리 목록을 페이징 및 정렬하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    ResponseEntity<List<ProjectGalleryResponseDto>> getProjectGallerys(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") int page,
            @Parameter(description = "페이지 당 항목 수", example = "20") int size,
            @Parameter(description = "정렬 기준 필드", example = "id") String sortBy,
            @Parameter(description = "정렬 순서 (ASC, DESC)", example = "ASC") SortOrder order
    );

    @Operation(summary = "프로젝트 갤러리 상세 조회", description = "프로젝트 ID를 통해 갤러리 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 프로젝트")
    })
    ResponseEntity<ProjectGalleryDetailResponseDto> getProjectGallery(
            @Parameter(description = "프로젝트 ID", example = "1", required = true) Long projectId
    );

    @Operation(summary = "프로젝트 갤러리 정보 수정", description = "프로젝트 ID를 통해 갤러리 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 프로젝트")
    })
    ResponseEntity<Void> updateProjectGallery(
            @Parameter(description = "프로젝트 ID", example = "1", required = true) Long projectId,
            @RequestBody(description = "수정할 프로젝트 갤러리 정보", required = true) ProjectGalleryUpdateRequestDto dto
    );
}
