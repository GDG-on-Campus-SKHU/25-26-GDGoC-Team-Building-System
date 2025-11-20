package com.skhu.gdgocteambuildingproject.projectgallery.repository;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryProjectMemberRepository extends JpaRepository<GalleryProjectMember, Long> {
    boolean existsByProjectIdAndUserIdAndRole(Long projectId, Long userId, MemberRole role);
}
