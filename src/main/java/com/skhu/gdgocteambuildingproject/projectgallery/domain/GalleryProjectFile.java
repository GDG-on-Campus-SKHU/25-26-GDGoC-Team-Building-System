package com.skhu.gdgocteambuildingproject.projectgallery.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProjectFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    private GalleryProject project;
}
