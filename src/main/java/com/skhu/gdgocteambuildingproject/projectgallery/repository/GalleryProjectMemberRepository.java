package com.skhu.gdgocteambuildingproject.projectgallery.repository;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GalleryProjectMemberRepository extends JpaRepository<GalleryProjectMember, Long> {
    boolean existsByProjectIdAndUserIdAndRole(
            Long projectId,
            Long userId,
            MemberRole role
    );

    List<GalleryProjectMember> findAllByUserId(Long userId);

    Optional<GalleryProjectMember> findByUserIdAndProjectId(Long userId, Long projectId);
}
