package com.skhu.gdgocteambuildingproject.global.aws.controller;

import com.skhu.gdgocteambuildingproject.global.aws.api.S3ImageControllerApi;
import com.skhu.gdgocteambuildingproject.global.aws.dto.response.FileUploadResponseDto;
import com.skhu.gdgocteambuildingproject.global.aws.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@PreAuthorize("hasAnyRole('SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS')")
@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class S3ImageController implements S3ImageControllerApi {

    private final S3ImageService s3ImageService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponseDto> uploadImage(
            @RequestPart(value = "imageFile", required = false) MultipartFile image,
            @RequestParam(name = "directory", defaultValue = "image") String directoryName
    ) {
        return ResponseEntity.ok(s3ImageService.upload(image, directoryName));
    }

    @Override
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long fileId) {
        s3ImageService.deleteFileById(fileId);
        return ResponseEntity.ok().build();
    }
}
