package com.skhu.gdgocteambuildingproject.projectgallery.model.security;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectMemberRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GalleryProjectAccessChecker {

    private final GalleryProjectMemberRepository galleryProjectMemberRepository;

    public boolean checkLeaderOrAdminPermission(
            Long projectId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        UserRole userRole = user.getRole();

        if (isAdmin(userRole)) {
            return true;
        }
        if (isMember(userRole)) {
            return isLeaderOfProject(projectId, user.getId());
        }
        return false;
    }

    private boolean isAdmin(UserRole userRole) {
        return userRole == UserRole.SKHU_ADMIN;
    }

    private boolean isMember(UserRole userRole) {
        return userRole == UserRole.SKHU_MEMBER;
    }

    private boolean isLeaderOfProject(
            Long projectId,
            Long userId
    ) {
        return galleryProjectMemberRepository.existsByProjectIdAndUserIdAndRole(
                projectId,
                userId,
                MemberRole.LEADER
        );
    }
}
