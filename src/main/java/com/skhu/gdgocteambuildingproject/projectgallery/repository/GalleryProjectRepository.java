package com.skhu.gdgocteambuildingproject.projectgallery.repository;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryProjectRepository extends JpaRepository<GalleryProject, Long> {
    List<GalleryProject> findByGenerationAndExhibitedTrueOrderByCreatedAtDesc(Generation generation);

    List<GalleryProject> findByProjectNameContainingAndExhibitedTrue(String keyword);

    List<GalleryProject> findByExhibitedTrueOrderByCreatedAtDesc();

    List<GalleryProject> findByProjectNameContaining(String projectName);
}
