package com.skhu.gdgocteambuildingproject.global.aws.service;

import com.skhu.gdgocteambuildingproject.global.aws.dto.response.FileUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface S3ImageService {
    FileUploadResponseDto upload(MultipartFile imageFile, String directoryName);

    void deleteFileById(Long fileId);
}
