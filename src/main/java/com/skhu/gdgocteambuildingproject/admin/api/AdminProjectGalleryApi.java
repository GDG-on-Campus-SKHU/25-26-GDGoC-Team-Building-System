package com.skhu.gdgocteambuildingproject.admin.api;

import com.skhu.gdgocteambuildingproject.admin.dto.projectGallery.ProjectGalleryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "관리자 프로젝트 갤러리 관리 API", description = "관리자용 프로젝트 갤러리 관리 API")
public interface AdminProjectGalleryApi {

    @Operation(summary = "프로젝트 갤러리에서 프로젝트명으로 검색", description = "프로젝트 갤러리에서 프로젝트명으로 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    ResponseEntity<List<ProjectGalleryResponseDto>> searchProjectGallery(String keyword);
}
