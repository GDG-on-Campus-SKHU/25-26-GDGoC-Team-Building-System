package com.skhu.gdgocteambuildingproject.user.service;

import com.skhu.gdgocteambuildingproject.global.util.PrincipalUtil;
import com.skhu.gdgocteambuildingproject.user.dto.UserIdResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Override
    public UserIdResponseDto getUserIdFromToken(Principal principal) {
        return UserIdResponseDto.builder()
                .userId(PrincipalUtil.getUserIdFrom(principal))
                .build();
    }
}
