package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.UserSelectOptionsDto;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UserSelectOptionsMapper {
    public UserSelectOptionsDto fromEnums() {
        return UserSelectOptionsDto.builder()
                .parts(Arrays.stream(Part.values()).toList())
                .positions(Arrays.stream(UserPosition.values()).toList())
                .generations(Arrays.stream(Generation.values())
                        .map(Generation::getLabel)
                        .toList())
                .build();
    }
}
