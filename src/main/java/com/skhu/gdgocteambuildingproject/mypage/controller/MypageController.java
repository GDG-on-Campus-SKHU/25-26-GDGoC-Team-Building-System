package com.skhu.gdgocteambuildingproject.mypage.controller;

import com.skhu.gdgocteambuildingproject.mypage.api.MypageControllerApi;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.service.MypageService;
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
public class MypageController implements MypageControllerApi {

    private final MypageService mypageService;

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileInfoResponseDto> getProfileByUserid(@PathVariable Long userId) {
        return ResponseEntity.ok(mypageService.getProfileByUserId(userId));
    }

    @Override
    @PutMapping("/{userId}")
    public ResponseEntity<ProfileInfoResponseDto> updateModifiableProfile(@PathVariable Long userId,
                                                                          @RequestBody ProfileInfoUpdateRequestDto profileInfoRequestDto) {
        return ResponseEntity.ok(mypageService.updateUserModifiableProfile(userId, profileInfoRequestDto));
    }
}
