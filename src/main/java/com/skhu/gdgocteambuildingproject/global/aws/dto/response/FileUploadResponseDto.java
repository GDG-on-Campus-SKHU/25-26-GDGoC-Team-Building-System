package com.skhu.gdgocteambuildingproject.global.aws.dto.response;

import com.skhu.gdgocteambuildingproject.global.aws.domain.File;

public record FileUploadResponseDto(
        Long id,
        String url,
        String originalName,
        String mimeType,
        long size
) {
    public static FileUploadResponseDto from(File file) {
        return new FileUploadResponseDto(
                file.getId(),
                file.getUrl(),
                file.getOriginalName(),
                file.getMimeType(),
                file.getSize()
        );
    }
}
