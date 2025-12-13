package com.skhu.gdgocteambuildingproject.projectgallery.service;

import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.MemberSearchListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.TokenUserInfoForProjectBuildingResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.req.GalleryProjectSaveRequestDto;

import java.security.Principal;

public interface GalleryProjectService {
    Long exhibitProject(GalleryProjectSaveRequestDto requestDto);

    GalleryProjectInfoResponseDto findCurrentGalleryProjectInfoByProjectId(Long projectId);

    GalleryProjectListResponseDto findGalleryProjects(String generation);

    MemberSearchListResponseDto searchMemberByName(String name);

    Long updateGalleryProjectByProjectId(
            Long projectId,
            GalleryProjectSaveRequestDto requestDto
    );

    TokenUserInfoForProjectBuildingResponseDto findExhibitorInfo(Principal principal);

//    void deleteGalleryProjectByProjectId(Long projectId);
}
