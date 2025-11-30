package com.skhu.gdgocteambuildingproject.global.aws.service;

import com.skhu.gdgocteambuildingproject.global.aws.domain.File;
import com.skhu.gdgocteambuildingproject.global.aws.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client s3Client;
    private final FileRepository fileRepository;
    private static final String BUCKET = "teambuildingsystem-bucket";
    private static final String URLREGION = "ap-northeast-2";

    public File uplaoad(MultipartFile multipartFile, String directoryName) {
        String originalName = multipartFile.getOriginalFilename();
        String s3FileName = createRandomFileName(originalName);
        String bucketPath = directoryName + "/" + s3FileName;

        putObjectToS3(multipartFile, bucketPath);

        String url = "https://" + BUCKET + ".s3." + URLREGION + ".amazonaws.com/" +bucketPath;

        File fileToUpload = File.ofUploadResult(
                originalName,
                s3FileName,
                bucketPath,
                url,
                multipartFile.getContentType(),
                multipartFile.getSize()
        );

        return fileRepository.save(fileToUpload);
    }

    private void putObjectToS3(MultipartFile multipartFile, String bucketPath) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(bucketPath)
                    .contentType(multipartFile.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(multipartFile.getBytes())
            );
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }

    private String createRandomFileName(String originalName) {
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        return UUID.randomUUID() + ext;
    }
}
