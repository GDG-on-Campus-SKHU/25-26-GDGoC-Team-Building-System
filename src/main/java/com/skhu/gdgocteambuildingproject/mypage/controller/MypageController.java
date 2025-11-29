package com.skhu.gdgocteambuildingproject.mypage.controller;

import com.skhu.gdgocteambuildingproject.mypage.api.MyPageControllerApi;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.service.MypageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController implements MyPageControllerApi {

    private final MypageServiceImpl mypageService;

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileInfoResponseDto> getProfileByUserid(@PathVariable Long userId) {
        return ResponseEntity.ok(mypageService.getProfileByUserId(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ProfileInfoResponseDto> updateProfile(@PathVariable Long userId,
                                                                @RequestBody ProfileInfoRequestDto profileInfoRequestDto) {
        return ResponseEntity.ok(mypageService.updateProfile(userId, profileInfoRequestDto));
    }
}
