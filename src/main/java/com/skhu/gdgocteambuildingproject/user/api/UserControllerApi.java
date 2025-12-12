package com.skhu.gdgocteambuildingproject.user.api;

import com.skhu.gdgocteambuildingproject.user.dto.UserIdResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

@Tag(
        name = "유저 정보 API",
        description = "마이페이지와 별개로 유저의 정보를 얻을 때 사용합니다. 별도의 권한은 필요 없으나 토큰을 요구합니다."
)
public interface UserControllerApi {
    @Operation(
            summary = "토큰으로 인식한 유저의 userId값 조회",
            description =
                    """
                    유저의 userId 값을 조회합니다.
                    
                    헤더에 Bearer 토큰을 필수로 포함해야 합니다.
                    """
    )
    ResponseEntity<UserIdResponseDto> getUserId(Principal principal);
}
