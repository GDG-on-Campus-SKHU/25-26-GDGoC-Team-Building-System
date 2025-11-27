package com.skhu.gdgocteambuildingproject.admin.api;

import com.skhu.gdgocteambuildingproject.admin.dto.activity.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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

    @Operation(summary = "카테고리 목록 정보 조회", description = "모든 카테고리의 정보(이름, 게시 여부)와 각 카테고리에 속한 게시글 개수를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<ActivityCategoryInfoResponseDto>> getCategoryInfos();


    @Operation(summary = "특정 카테고리의 게시글 목록 조회", description = "카테고리 ID를 통해 해당 카테고리에 속한 모든 게시글 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리 ID")
    })
    ResponseEntity<List<ActivityResponseDto>> getActivitiesByCategory(
            @Parameter(description = "조회할 카테고리의 ID", required = true) @PathVariable Long categoryId
    );
}
