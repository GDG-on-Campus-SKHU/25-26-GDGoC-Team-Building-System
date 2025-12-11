package com.skhu.gdgocteambuildingproject.mypage.service;

import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.TechStackOptionResponseDto;

import java.util.List;

public interface MypageService {
    ProfileInfoResponseDto getProfileByUserPrincipal(Long userId);

    ProfileInfoResponseDto updateUserModifiableProfile(Long userId, ProfileInfoUpdateRequestDto requestDto);

    ProfileInfoResponseDto getProfileByIdeaMemberId(Long ideaMemberId);

    List<TechStackOptionResponseDto> getAllTechStackOptions();
}
