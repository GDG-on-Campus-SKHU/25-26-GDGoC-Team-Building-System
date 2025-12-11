package com.skhu.gdgocteambuildingproject.Idea.controller;

import com.skhu.gdgocteambuildingproject.teambuilding.service.IdeaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ideas")
@PreAuthorize("hasAnyRole('SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS')")
@Tag(
        name = "아이디어 API",
        description = "'SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS' 중 하나의 권한이 있어야 호출할 수 있습니다."
)
public class IdeaController {

    private static final ResponseEntity<Void> NO_CONTENT = ResponseEntity.noContent().build();

    private final IdeaService ideaService;

    @DeleteMapping("/{ideaId}/members/{memberId}")
    @Operation(
            summary = "멤버 삭제",
            description = """
                    아이디어의 팀원을 제거합니다.
                    팀장(CREATOR)만 호출할 수 있습니다.
                    
                    이미 확정된(confirmed = true) 멤버라면 제거할 수 없습니다.
                    
                    memberId: 팀원의 userId
                    """
    )
    public ResponseEntity<Void> deleteMember(
            Principal principal,
            @PathVariable long ideaId,
            @PathVariable long memberId
    ) {
        long userId = findUserIdBy(principal);

        ideaService.removeMember(userId, ideaId, memberId);

        return NO_CONTENT;
    }

    private long findUserIdBy(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
