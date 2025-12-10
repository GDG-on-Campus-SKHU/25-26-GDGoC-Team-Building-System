package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.UserSearchResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserSearchMapper {

    public UserSearchResponseDto map(User user, String searchGenerationLabel) {
        return UserSearchResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .school(user.getSchool())
                // 기수가 여럿일 때 검색 조건과 일치하는 기수를 응답하기 위해, 파라미터로 받은 기수 사용
                .generation(searchGenerationLabel)
                .part(user.getPart())
                .build();
    }
}

