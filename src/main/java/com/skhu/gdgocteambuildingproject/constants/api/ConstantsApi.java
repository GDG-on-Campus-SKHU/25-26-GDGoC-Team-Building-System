package com.skhu.gdgocteambuildingproject.constants.api;

import com.skhu.gdgocteambuildingproject.constants.dto.GenerationResponseDto;
import com.skhu.gdgocteambuildingproject.constants.dto.PartResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "상수 조회 API", description = "상수 값을 조회하는 API입니다. 별도 권한을 요구하지 않습니다.")
public interface ConstantsApi {

    @Operation(
            summary = "기수 목록 조회",
            description = """
                    사용할 수 있는 기수 목록을 조회합니다.
                    기수 목록은 오름차순으로 제공됩니다.
                    
                    ex) ["23-24", "24-25", "25-26"]
                    """
    )
    ResponseEntity<List<GenerationResponseDto>> getGenerations();

    @Operation(
            summary = "파트 목록 조회",
            description = """
                    사용할 수 있는 파트 목록을 조회합니다.
                    
                    name: 파트 코드 (PM, DESIGN, WEB, MOBILE, BACKEND, AI)
                    koreanName: 파트 한글명 (기획, 디자인, 프론트엔드(웹), 프론트엔드(모바일), 백엔드, AI/ML)
                    """
    )
    ResponseEntity<List<PartResponseDto>> getParts();
}
