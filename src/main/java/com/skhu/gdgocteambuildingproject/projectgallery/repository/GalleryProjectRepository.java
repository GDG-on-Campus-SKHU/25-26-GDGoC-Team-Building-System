package com.skhu.gdgocteambuildingproject.projectgallery.repository;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryProjectRepository extends JpaRepository<GalleryProject, Long> {
    List<GalleryProject> findByGenerationOrderByCreatedAtDesc(String generation);
}
