package com.skhu.gdgocteambuildingproject.global.aws.service;

import com.skhu.gdgocteambuildingproject.global.aws.domain.File;
import com.skhu.gdgocteambuildingproject.global.aws.dto.response.FileUploadResponseDto;
import com.skhu.gdgocteambuildingproject.global.aws.repository.FileRepository;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ImageService {

    private static final List<String> ALLOWED_EXTENSION_LIST = List.of("jpg", "jpeg", "png", "gif");

    private final S3Client s3Client;
    private final FileRepository fileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public FileUploadResponseDto upload(MultipartFile imageFile, String directoryName) {
        validateImageFile(imageFile);

        File file = uploadImageToS3(imageFile, directoryName);
        saveFileToDatabaseOrRollbackS3Upload(file);

        return FileUploadResponseDto.from(file);
    }

    @Transactional
    public void deleteFileById(Long fileId) {
        File file = findFileByIdOrThrow(fileId);
        deleteImageFromS3(file.getBucketPath());

        fileRepository.delete(file);
    }

    // 업로드 관련 로직 시작
    private void validateImageFile(MultipartFile imageFile) {
        validateImageFileIsEmpty(imageFile);
        validateImageFileName(imageFile.getOriginalFilename());
    }

    private void validateImageFileIsEmpty(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty() || imageFile.getOriginalFilename() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ExceptionMessage.EMPTY_FILE.getMessage()
            );
        }
    }

    private void validateImageFileName(String originalFilename) {
        if (!StringUtils.hasText(originalFilename)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ExceptionMessage.NO_FILE_NAME.getMessage()
            );
        }

        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex == -1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ExceptionMessage.NO_FILE_EXTENSION.getMessage()
            );
        }

        String extension = originalFilename.substring(dotIndex + 1).toLowerCase();
        if (!ALLOWED_EXTENSION_LIST.contains(extension)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ExceptionMessage.INVALID_FILE_EXTENSION.getMessage()
            );
        }
    }

    private File uploadImageToS3(MultipartFile imageFile, String directoryName) {
        String originalFilename = imageFile.getOriginalFilename();
        String s3FileNameWithRandomString = createS3FileNameWithRandomString(imageFile.getOriginalFilename());
        String bucketPath = makeBucketPath(directoryName, s3FileNameWithRandomString);
        String url = generateFileUrl(bucketPath);
        String mimeType = resolveContentType(imageFile);
        long size = imageFile.getSize();

        putObjectToS3Bucket(imageFile, bucketPath, mimeType, size);

        return File.ofUploadResult(
                originalFilename,
                s3FileNameWithRandomString,
                bucketPath,
                url,
                mimeType,
                size
        );
    }

    private String createS3FileNameWithRandomString(String originalFilename) {
        String randomPrefix = UUID.randomUUID().toString().substring(0, 10);

        return randomPrefix + "_" + originalFilename;
    }

    private String makeBucketPath(String directoryName, String s3FileName) {
        if (!StringUtils.hasText(directoryName)) {
            return s3FileName;
        }

        // 문자열 맨앞 혹은 맨뒤에 연속된 / 들 제거
        String normalizedDirectory = directoryName.replaceAll("^/+", "").replaceAll("/+$", "");
        return normalizedDirectory + "/" + s3FileName;
    }

    private String resolveContentType(MultipartFile imageFile) {
        String contentType = imageFile.getContentType();
        if (StringUtils.hasText(contentType) && contentType.startsWith("image/")) {
            return contentType;
        }
        String extension = extractExtension(imageFile.getOriginalFilename());

        return "image/" + extension;
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

    private void putObjectToS3Bucket(MultipartFile imageFile,
                                     String bucketPath,
                                     String mimeType,
                                     long size) {
        try (InputStream is = imageFile.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(bucketPath)
                    .contentType(mimeType)
                    .contentLength(size)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(is, size));
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionMessage.IO_EXCEPTION_ON_IMAGE_UPLOAD.getMessage()
            );
        } catch (S3Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionMessage.PUT_OBJECT_EXCEPTION.getMessage()
            );
        }
    }

    private String generateFileUrl(String bucketPath) {
        return s3Client.utilities().getUrl(
                GetUrlRequest.builder()
                        .bucket(bucketName)
                        .key(bucketPath)
                        .build()
        ).toString();
    }

    private File findFileByIdOrThrow(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.FILE_NOT_EXIST.getMessage()));
    }

    private void saveFileToDatabaseOrRollbackS3Upload(File file) {
        try {
            fileRepository.save(file);
        } catch (Exception e) {
            deleteImageFromS3(file.getBucketPath());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionMessage.FILE_UPLOAD_TRANSACTION_FAILED.getMessage(),
                    e
            );
        }
    }

    // 삭제 관련 로직
    private void deleteImageFromS3(String bucketPath) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(bucketPath)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionMessage.IO_EXCEPTION_ON_IMAGE_DELETE.getMessage()
            );
        }
    }
}
