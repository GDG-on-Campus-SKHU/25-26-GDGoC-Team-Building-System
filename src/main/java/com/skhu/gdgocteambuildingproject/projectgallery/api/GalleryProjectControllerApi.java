package com.skhu.gdgocteambuildingproject.projectgallery.api;

import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.MemberSearchListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.req.GalleryProjectSaveRequestDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectListResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(
        name = "프로젝트 갤러리 API",
        description = "조회는 모든 사용자가 호출할 수 있지만, 생성은 'SKHU_ADMIN', 'SKHU_MEMBER' 중 하나의 권한이 필요하고, " +
                "수정과 삭제는 'SKHU_ADMIN'이나 해당 프로젝트의 'LEADER'만 호출할 수 있습니다."
)
public interface GalleryProjectControllerApi {
    @Operation(
            summary = "프로젝트 갤러리에 새로운 프로젝트 전시",
            description =
                    """
                    프로젝트 갤러리에 새로운 프로젝트를 전시합니다.
                    
                    팀원 선택창에서 선택한 멤버 목록과, 파일 업로드 후 반환된 fileId 리스트를 body로 받습니다.
                    
                    ServiceStatus: IN_SERVICE(운영 중), NOT_IN_SERVICE(미운영 중)
                    
                    MemberRole: LEADER(팀장), MEMBER(팀원)
                    
                    PART: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "프로젝트 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터가 유효하지 않거나, 필요한 필드를 입력하지 않음"),
            @ApiResponse(responseCode = "404", description = "leaderId에 해당하는 유저가 없거나, fileId에 해당하는 파일이 없음"),
    })
    ResponseEntity<Long> exhibitProject(@RequestBody GalleryProjectSaveRequestDto requestDto);

    @Operation(
            summary = "갤러리에 있는 프로젝트 상세 조회",
            description =
                    """
                    프로젝트 갤러리에 전시되어 있는 프로젝트의 상세 정보를 조회합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "projectId에 해당하는 프로젝트가 없음"),
    })
    ResponseEntity<GalleryProjectInfoResponseDto> findCurrentGalleryProjectInfoByProjectId(@PathVariable Long projectId);

    @Operation(
            summary = "갤러리에 있는 프로젝트 목록 조회",
            description =
                    """
                    프로젝트 갤러리에 전시되어 있는 프로젝트 목록을 조회합니다. 정렬은 최신순으로 나열합니다.
                    
                    generation(기수)를 parameter로 필터링하고, parameter를 넣지 않으면 필터링 없이 모든 내용을 조회합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "아무런 프로젝트가 없음"),
    })
    ResponseEntity<GalleryProjectListResponseDto> findGalleryProjects(@RequestParam(defaultValue = "") String generation);

    @Operation(
            summary = "갤러리 생성시 팀원 등록을 위한 유저 목록 조회",
            description =
                    """
                    프로젝트 갤러리에서 팀원을 등록하기 위해 모든 유저를 검색합니다.
                    
                    해당 유저가 해당 팀의 구성원으로 참여했는지에 대한 참여 여부는 프론트에서 관리합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 목록 조회 성공")
    })
    ResponseEntity<MemberSearchListResponseDto> searchMemberList(@RequestParam String name);

    @Operation(
            summary = "프로젝트 갤러리에 존재하는 프로젝트의 정보 수정",
            description =
                    """
                    프로젝트 갤러리에 전시되어있는 프로젝트를 수정합니다.
                    
                    생성 때와 같이, 멤버 목록과 fileId 리스트를 body로 받습니다.
                    
                    ServiceStatus: IN_SERVICE(운영 중), NOT_IN_SERVICE(미운영 중)
                    
                    MemberRole: LEADER(팀장), MEMBER(팀원)
                    
                    PART: PM, DESIGN, WEB, MOBILE, BACKEND, AI
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 수정 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터가 유효하지 않거나, 필요한 필드를 입력하지 않음"),
            @ApiResponse(responseCode = "404", description = "leaderId와 fileId에 해당하는 유저나 파일이 없거나, projectId에 해당하는 프로젝트가 없음"),
    })
    ResponseEntity<Long> updateProject(@PathVariable Long projectId, @RequestBody GalleryProjectSaveRequestDto requestDto);

    @Operation(
            summary = "프로젝트 갤러리에 존재하는 프로젝트 삭제",
            description =
                    """
                    프로젝트 갤러리에 전시되어있는 프로젝트를 삭제합니다.
                    
                    id에 해당하는 프로젝트가 없을 경우, 404 응답을 반환합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "프로젝트 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "projectId에 해당하는 프로젝트가 없음"),
    })
    ResponseEntity<Void> deleteProject(@PathVariable Long projectId);
}
