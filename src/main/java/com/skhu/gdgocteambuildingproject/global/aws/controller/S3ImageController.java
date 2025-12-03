package com.skhu.gdgocteambuildingproject.global.aws.controller;

import com.skhu.gdgocteambuildingproject.global.aws.api.S3ImageControllerApi;
import com.skhu.gdgocteambuildingproject.global.aws.domain.File;
import com.skhu.gdgocteambuildingproject.global.aws.dto.response.FileUploadResponseDto;
import com.skhu.gdgocteambuildingproject.global.aws.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Override
    public ResponseEntity<FileUploadResponseDto> uploadImage(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(name = "directory", defaultValue = "image") String directoryName
    ) {
        File uploadedFile = s3ImageService.upload(image, directoryName);
        return ResponseEntity.ok(FileUploadResponseDto.from(uploadedFile));
    }

    @DeleteMapping("/delete")
    @Override
    public ResponseEntity<Void> deleteImage(@RequestParam(name = "url") String imageUrl) {
        s3ImageService.deleteImageFromS3(imageUrl);
        return ResponseEntity.ok().build();
    }
}
