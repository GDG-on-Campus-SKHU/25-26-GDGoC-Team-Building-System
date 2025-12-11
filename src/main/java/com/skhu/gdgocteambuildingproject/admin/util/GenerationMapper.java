package com.skhu.gdgocteambuildingproject.admin.util;

import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserGenerationResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenerationMapper {
    public List<ApprovedUserGenerationResponseDto> toMainGenerationDtos(User user) {
        return user.getGeneration().stream()
                .filter(gen -> gen.isMain())
                .map(gen -> new ApprovedUserGenerationResponseDto(
                        gen.getId(),
                        gen.getGeneration().getLabel(),
                        gen.getPosition().name(),
                        gen.isMain()
                ))
                .toList();
    }
}
