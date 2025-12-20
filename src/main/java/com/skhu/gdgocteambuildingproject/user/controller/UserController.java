package com.skhu.gdgocteambuildingproject.user.controller;

import com.skhu.gdgocteambuildingproject.user.api.UserControllerApi;
import com.skhu.gdgocteambuildingproject.user.dto.UserIdResponseDto;
import com.skhu.gdgocteambuildingproject.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserController implements UserControllerApi {

    private final UserService userService;

    @Override
    @GetMapping("/id")
    public ResponseEntity<UserIdResponseDto> getUserId(Principal principal) {
        return ResponseEntity.ok(userService.getUserIdFromToken(principal));
    }
}
