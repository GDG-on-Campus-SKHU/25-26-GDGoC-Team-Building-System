package com.skhu.gdgocteambuildingproject.activity.api;

import com.skhu.gdgocteambuildingproject.activity.dto.ActivitySummaryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(
        name = "액티비티 API",
        description = "모든 사용자가 액티비티에 등록된 모든 게시글 조회가 가능합니다."
)
public interface ActivityControllerApi {

    @Operation(
            summary = "액티비티에 등록된 모든 게시글 조회",
            description =
                    """
                    액티비티에 등록된 모든 게시글을 조회합니다.
                    
                    게시글 정보에는 카테고리 정보와 게시글 정보가 포함됩니다.
                    """
    )
    @ApiResponse(responseCode = "200", description = "액티비티 목록 조회 성공")
    ResponseEntity<List<ActivitySummaryResponseDto>> getAllActivity();
}
