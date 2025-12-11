package com.skhu.gdgocteambuildingproject.mypage.controller;

import com.skhu.gdgocteambuildingproject.global.jwt.service.UserPrincipal;
import com.skhu.gdgocteambuildingproject.mypage.api.MypageControllerApi;
import com.skhu.gdgocteambuildingproject.mypage.dto.request.ProfileInfoUpdateRequestDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.ProfileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.dto.response.TechStackOptionResponseDto;
import com.skhu.gdgocteambuildingproject.mypage.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mypage")
@PreAuthorize("hasAnyRole('SKHU_ADMIN', 'SKHU_MEMBER', 'OTHERS')")
@RequiredArgsConstructor
public class MypageController implements MypageControllerApi {

    private final MypageService mypageService;


    @Override
    @GetMapping
    public ResponseEntity<ProfileInfoResponseDto> getProfileByUserPrincipal(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long currentUserId = userPrincipal.getUser().getId();
        return ResponseEntity.ok(mypageService.getProfileByUserPrincipal(currentUserId));
    }

    @Override
    @PutMapping
    public ResponseEntity<ProfileInfoResponseDto> updateUserModifiableProfile(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                              @RequestBody ProfileInfoUpdateRequestDto profileInfoRequestDto) {
        Long currentUserId = userPrincipal.getUser().getId();
        return ResponseEntity.ok(mypageService.updateUserModifiableProfile(currentUserId, profileInfoRequestDto));
    }

    @Override
    @GetMapping("/profile/{ideaMemberId}")
    public ResponseEntity<ProfileInfoResponseDto> getProfileByIdeaMemberId(@PathVariable Long ideaMemberId) {
        return ResponseEntity.ok(mypageService.getProfileByIdeaMemberId(ideaMemberId));
    }

    @Override
    @GetMapping("/tech-stacks")
    public ResponseEntity<List<TechStackOptionResponseDto>> getAllTechStacks() {
        return ResponseEntity.ok(mypageService.getAllTechStackOptions());
    }
}
