package com.skhu.gdgocteambuildingproject.user.service;

import com.skhu.gdgocteambuildingproject.user.dto.UserIdResponseDto;

import java.security.Principal;

public interface UserService {

    UserIdResponseDto getUserIdFromToken(Principal principal);
}
