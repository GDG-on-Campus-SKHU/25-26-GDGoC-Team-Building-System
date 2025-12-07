package com.skhu.gdgocteambuildingproject.global.aws.api;

import com.skhu.gdgocteambuildingproject.global.aws.dto.response.FileUploadResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(
        name = "이미지 API",
        description = "이미지를 S3에 업로드 및 삭제하는 API"
)
public interface S3ImageControllerApi {

    @Operation(
            summary = "이미지 업로드",
            description = """
                    S3에 이미지를 업로드합니다.
                    이미지 업로드에 성공하면 이미지의 주소가 반환됩니다.
                    
                    JSON이 아닌 multipart/form-data 형식으로 요청해야 합니다.
                    directory 쿼리 파라미터로 S3 내 디렉토리 경로를 지정할 수 있습니다.
                    경로 미지정시 s3 버킷의 /image 폴더에 저장됩니다.
                      (예: profile, idea, project 등)
                    """
    )
    @ApiResponse(responseCode = "200")
    ResponseEntity<FileUploadResponseDto> uploadImage(
            @Parameter(description = "업로드할 이미지 파일", required = true)
            @RequestPart(value = "imageFile", required = false) MultipartFile image,

            @Parameter(description = "S3 내에 이미지를 저장할 디렉토리 이름 (예: profile, idea, project)")
            @RequestParam(name = "directory", required = false) String directoryName
    );

    @Operation(
            summary = "이미지 삭제",
            description = """
                    파일 ID로 이미지 파일을 삭제합니다.
                    실제로 DB에서 제거되어 복구할 수 없습니다.
                    """
    )
    @ApiResponse(responseCode = "200")
    ResponseEntity<Void> deleteImage(
            @Parameter(description = "파일 ID", example = "1") Long fileId
    );
}
