package com.skhu.gdgocteambuildingproject.admin.dto.profile;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.mypage.dto.TechStackDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.UserLinkDto;
import lombok.Builder;

import java.util.List;

@Builder
public record UserProfileResponseDto(
        Long userId,
        String name,
        String school,
        //머지 된후 리스트 추가
        Part part,
        List<TechStackDto> techStacks,
        List<UserLinkDto> userLinks,
        String introduction
) {
}
