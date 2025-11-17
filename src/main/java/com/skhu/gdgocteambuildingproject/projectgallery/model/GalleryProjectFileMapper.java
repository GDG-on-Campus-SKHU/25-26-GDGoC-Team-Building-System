package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectFile;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.GalleryProjectFileInfoResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GalleryProjectFileMapper {

    public List<GalleryProjectFileInfoResponseDto> map(List<GalleryProjectFile> files) {
        return files.stream()
                .map(this::fromEntity)
                .toList();
    }

    private GalleryProjectFileInfoResponseDto fromEntity(GalleryProjectFile file) {
        return GalleryProjectFileInfoResponseDto.builder()
                .fileUrl(file.getFile().getUrl())
                .build();
    }
}
