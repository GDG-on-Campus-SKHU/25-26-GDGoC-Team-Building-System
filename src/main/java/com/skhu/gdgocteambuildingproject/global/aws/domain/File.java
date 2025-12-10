package com.skhu.gdgocteambuildingproject.global.aws.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class File extends BaseEntity {

    private String originalName;
    private String s3FileName;
    private String bucketPath;
    private String url;
    private String mimeType;
    private long size;

    public static File ofUploadResult(String originalName,
                                      String s3FileName,
                                      String bucketPath,
                                      String url,
                                      String mimeType,
                                      long size) {
        return new File(
                originalName,
                s3FileName,
                bucketPath,
                url,
                mimeType,
                size
        );
    }
}
