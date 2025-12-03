package com.skhu.gdgocteambuildingproject.global.aws.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseEntity {

    private String originalName;
    private String s3FileName;
    private String bucketPath;
    private String url;
    private String mimeType;
    private long size;

    @Builder
    private File(String originalName,
                 String s3FileName,
                 String bucketPath,
                 String url,
                 String mimeType,
                 long size) {
        this.originalName = originalName;
        this.s3FileName = s3FileName;
        this.bucketPath = bucketPath;
        this.url = url;
        this.mimeType = mimeType;
        this.size = size;
    }

    public static File ofUploadResult(String originalName,
                                      String s3FileName,
                                      String bucketPath,
                                      String url,
                                      String mimeType,
                                      long size) {
        return File.builder()
                .originalName(originalName)
                .s3FileName(s3FileName)
                .bucketPath(bucketPath)
                .url(url)
                .mimeType(mimeType)
                .size(size)
                .build();
    }
}
