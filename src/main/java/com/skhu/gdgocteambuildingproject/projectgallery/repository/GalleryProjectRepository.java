package com.skhu.gdgocteambuildingproject.projectgallery.repository;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryProjectRepository extends JpaRepository<GalleryProject, Long> {
}
