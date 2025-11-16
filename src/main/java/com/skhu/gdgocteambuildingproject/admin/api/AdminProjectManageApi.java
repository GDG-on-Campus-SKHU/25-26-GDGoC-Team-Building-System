package com.skhu.gdgocteambuildingproject.admin.api;

import com.skhu.gdgocteambuildingproject.admin.dto.project.ProjectCreateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "관리자 프로젝트 관리 API", description = "관리자용 프로젝트 관리 API입니다")
public interface AdminProjectManageApi {

    @Operation(
            summary = "새 프로젝트 등록",
            description = "새로운 프로젝트를 생성합니다."
    )
    ResponseEntity<Void> createNewProject(ProjectCreateRequestDto requestDto);
}
