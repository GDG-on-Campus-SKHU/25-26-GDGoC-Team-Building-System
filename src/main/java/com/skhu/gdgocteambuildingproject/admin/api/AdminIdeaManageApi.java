package com.skhu.gdgocteambuildingproject.admin.api;

import com.skhu.gdgocteambuildingproject.admin.dto.idea.AdminIdeaDetailResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.idea.IdeaTitleInfoIncludeDeletedPageResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.request.IdeaUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.global.pagination.SortOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "관리자 아이디어 관리 API", description = "관리자용 아이디어 관리 API입니다")
public interface AdminIdeaManageApi {

    String DEFAULT_PAGE = "0";
    String DEFAULT_SIZE = "20";
    String DEFAULT_SORT_BY = "id";
    String DEFAULT_ORDER = "ASC";

    @Operation(
            summary = "아이디어 삭제",
            description = """
                    아이디어를 제거합니다.
                    관련된 지원 정보도 모두 같이 제거됩니다.
                    
                    사용자가 본인의 아이디어를 삭제할 때와 달리, 실제로 DB에서 제거되어 복구할 수 없습니다.
                    """
    )
    ResponseEntity<Void> deleteIdea(
            @Parameter(description = "삭제할 아이디어의 ID") long ideaId
    );

    @Operation(
            summary = "아이디어 조회",
            description = """
                    아이디어를 조회합니다.
                    소프트 딜리트된 아이디어도 포함해 조회합니다.
                    
                    아이디어의 소프트 딜리트 여부도 같이 반환합니다.
                    
                    sortBy(정렬 기준): id(순번), topic(주제), title(제목), introduction(한줄 소개), description(설명)
                    
                    order: ASC 또는 DESC
                    """
    )
    ResponseEntity<IdeaTitleInfoIncludeDeletedPageResponseDto> findIdeas(
            @Parameter(description = "프로젝트 ID") long projectId,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") int page,
            @Parameter(description = "페이지 당 항목 수", example = "20") int size,
            @Parameter(description = "정렬 기준 필드명 (id, topic, title, introduction, description)", example = "id") String sortBy,
            @Parameter(description = "정렬 순서 (ASC 또는 DESC)") SortOrder order
    );

    @Operation(
            summary = "아이디어 상세 조회",
            description = """
                    프로젝트에 게시된 아이디어 하나의 상세 정보를 조회합니다.
                    소프트 딜리트된 아이디어도 조회할 수 있습니다.
                    
                    아이디어의 소프트 딜리트 여부도 같이 반환합니다.
                    
                    part: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    """
    )
    ResponseEntity<AdminIdeaDetailResponseDto> findIdeaDetail(
            @Parameter(description = "프로젝트 ID") long projectId,
            @Parameter(description = "아이디어 ID") long ideaId
    );

    @Operation(
            summary = "아이디어 수정",
            description = """
                    아이디어를 수정합니다.
                    
                    creatorPart, part: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    """
    )
    ResponseEntity<Void> updateIdea(
            @Parameter(description = "아이디어 ID") long ideaId,
            @RequestBody IdeaUpdateRequestDto requestDto
    );

    @Operation(
            summary = "아이디어 복원",
            description = """
                    소프트 딜리트된 아이디어를 복원합니다.
                    
                    해당 사용자가 이미 다른 아이디어를 게시했다면 4XX를 응답합니다.
                    삭제된 아이디어가 아니라면 4XX를 응답합니다.
                    """
    )
    ResponseEntity<Void> restoreIdea(
            @Parameter(description = "아이디어 ID") long ideaId
    );
}
