package com.skhu.gdgocteambuildingproject.mypage.service;

import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;

public interface MypageService {
    ProfileInfoResponseDto getProfileByUserId(Long userId);

    ProfileInfoResponseDto updateUserModifiableProfile(Long userId, ProfileInfoUpdateRequestDto requestDto);

    ProfileInfoResponseDto getProfileByIdeaMemberId(Long ideaMemberId);
}
