package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.project.GenerationResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminConstantsServiceImpl implements AdminConstantsService {

    @Override
    public List<GenerationResponseDto> getGenerations() {
        return Arrays.stream(Generation.values())
                .sorted(Comparator.comparing(Generation::ordinal))
                .map(GenerationResponseDto::from)
                .toList();
    }
}
