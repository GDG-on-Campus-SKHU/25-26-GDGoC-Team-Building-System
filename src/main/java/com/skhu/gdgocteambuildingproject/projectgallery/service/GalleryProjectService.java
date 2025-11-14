package com.skhu.gdgocteambuildingproject.projectgallery.service;

import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.MemberSearchListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.info.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.info.GalleryProjectListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.create.GalleryProjectCreateRequestDto;

public interface GalleryProjectService {
    Long exhibitProject(GalleryProjectCreateRequestDto requestDto);

    GalleryProjectInfoResponseDto findCurrentGalleryProjectInfoByProjectId(Long projectId);

    GalleryProjectListResponseDto findGalleryProjects(String generation);

    MemberSearchListResponseDto searchMemberByName(String name);
}
