package com.skhu.gdgocteambuildingproject.projectgallery.repository;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryProjectMemberRepository extends JpaRepository<GalleryProjectMember, Integer> {
    List<GalleryProjectMember> findByProjectId(Long projectId);

    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
}
