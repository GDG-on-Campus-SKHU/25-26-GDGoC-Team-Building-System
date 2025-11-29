package com.skhu.gdgocteambuildingproject.mypage.service;

import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;

public interface MypageService {
    ProfileInfoResponseDto getProfileByUserId(Long userId);

    ProfileInfoResponseDto updateProfile(Long userId, ProfileInfoRequestDto requestDto);
}
