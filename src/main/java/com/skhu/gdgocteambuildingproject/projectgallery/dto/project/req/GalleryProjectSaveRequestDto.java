package com.skhu.gdgocteambuildingproject.projectgallery.dto.project.req;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectMemberInfoDto;

import java.util.List;

public record GalleryProjectSaveRequestDto(
        String projectName,
        String generation,
        String shortDescription,
        ServiceStatus serviceStatus,
        String description,
        Long leaderId,
        List<GalleryProjectMemberInfoDto> members,
        List<Long> fileIds
) {
}
