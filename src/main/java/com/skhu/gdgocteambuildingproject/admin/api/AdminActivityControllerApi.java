package com.skhu.gdgocteambuildingproject.admin.api;

import com.skhu.gdgocteambuildingproject.admin.dto.activity.ActivitySaveRequestDto;
import com.skhu.gdgocteambuildingproject.admin.dto.activity.PostResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.activity.PostSaveDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "관리자 액티비티 관리 API", description = "관리자용 액티비티 게시글 관리 API")
public interface AdminActivityControllerApi {

    @Operation(summary = "액티비티 게시글 생성", description = "카테고리와 액티비티 게시글 목록을 받아 한 번에 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공"),
    })
    ResponseEntity<Void> createActivity(@RequestBody ActivitySaveRequestDto activitySaveRequestDto);


    @Operation(summary = "액티비티 게시글 수정", description = "특정 액티비티 게시글의 내용을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    ResponseEntity<PostResponseDto> updateActivityPost(
            @Parameter(description = "게시글 ID", required = true) Long postId,
            @RequestBody PostSaveDto postSaveDto
    );
}
