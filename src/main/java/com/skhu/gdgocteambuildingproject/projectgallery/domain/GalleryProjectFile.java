package com.skhu.gdgocteambuildingproject.projectgallery.domain;

import com.skhu.gdgocteambuildingproject.global.aws.File;
import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProjectFile extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private GalleryProject project;
}
