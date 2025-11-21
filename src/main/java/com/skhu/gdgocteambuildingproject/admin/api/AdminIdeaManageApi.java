package com.skhu.gdgocteambuildingproject.admin.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "관리자 아이디어 관리 API", description = "관리자용 아이디어 관리 API입니다")
public interface AdminIdeaManageApi {

    @Operation(
            summary = "아이디어 삭제",
            description = """
                    아이디어를 제거합니다.
                    관련된 지원 정보도 모두 같이 제거됩니다.
                    
                    사용자가 본인의 아이디어를 삭제할 때와 달리, 실제로 DB에서 제거되어 복구할 수 없습니다.
                    """
    )
    ResponseEntity<Void> deleteIdea(long ideaId);
}
