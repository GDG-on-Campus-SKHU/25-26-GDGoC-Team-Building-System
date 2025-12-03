package com.skhu.gdgocteambuildingproject.global.aws.service;

import com.skhu.gdgocteambuildingproject.global.aws.domain.File;
import com.skhu.gdgocteambuildingproject.global.aws.repository.FileRepository;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ImageService {

    private final S3Client s3Client;
    private final FileRepository fileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public File upload(MultipartFile image, String directoryName) {
        validateImage(image);
        return tryUploadImage(image, directoryName);
    }

    public void deleteImageFromS3(String imageAddress) {
        String key = getKeyFromImageAddress(imageAddress);
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionMessage.IO_EXCEPTION_ON_IMAGE_DELETE.getMessage()
            );
        }
    }

    private void validateImage(MultipartFile image) {
        if (image == null || image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ExceptionMessage.EMPTY_FILE.getMessage()
            );
        }
    }

    private File tryUploadImage(MultipartFile image, String dirName) {
        String originalFilename = image.getOriginalFilename();
        validateImageFileExtension(image.getOriginalFilename());

        try {
            return uploadImageToS3(image, originalFilename, dirName);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionMessage.IO_EXCEPTION_ON_IMAGE_UPLOAD.getMessage()
            );
        }
    }

    private void validateImageFileExtension(String filename) {
        if (filename == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ExceptionMessage.NO_FILE_NAME.getMessage()
            );
        }

        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ExceptionMessage.NO_FILE_NAME.getMessage()
            );
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtensionList = List.of("jpg", "jpeg", "png", "gif");

        if (!allowedExtensionList.contains(extension)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ExceptionMessage.INVALID_FILE_EXTENSION.getMessage()
            );
        }
    }

    private File uploadImageToS3(MultipartFile image, String originalFilename, String dirName) throws IOException {
        String extension = extractExtension(originalFilename);
        String s3FileName = createS3FileName(originalFilename);
        String key = buildKey(dirName, s3FileName);

        String mimeType = resolveContentType(image, extension);
        long size = image.getSize();

        try (InputStream is = image.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(mimeType)
                    .contentLength(size)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(is, size));
        } catch (S3Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionMessage.PUT_OBJECT_EXCEPTION.getMessage()
            );
        }

        String url = s3Client.utilities().getUrl(
                GetUrlRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()
        ).toString();

        File file = File.ofUploadResult(
                originalFilename,
                s3FileName,
                key,
                url,
                mimeType,
                size
        );

        return fileRepository.save(file);
    }

    private String extractExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ExceptionMessage.NO_FILE_EXTENSION.getMessage()
            );
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    private String createS3FileName(String originalFilename) {
        String randomPrefix = UUID.randomUUID().toString().substring(0, 10);
        return randomPrefix + "_" + originalFilename;
    }

    private String buildKey(String dirName, String s3FileName) {
        if (dirName == null || dirName.isBlank()) {
            return s3FileName;
        }
        return dirName + "/" + s3FileName;
    }

    private String resolveContentType(MultipartFile image, String extension) {
        String contentType = image.getContentType();
        if (contentType != null && contentType.startsWith("image/")) {
            return contentType;
        }
        return "image/" + extension;
    }

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URI uri = new URI(imageAddress);
            String path = uri.getPath();
            String decodingKey = URLDecoder.decode(path, StandardCharsets.UTF_8);

            return decodingKey.startsWith("/") ? decodingKey.substring(1) : decodingKey;
        } catch (URISyntaxException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionMessage.IO_EXCEPTION_ON_IMAGE_DELETE.getMessage()
            );
        }
    }
}
