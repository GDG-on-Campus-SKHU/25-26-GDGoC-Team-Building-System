package com.skhu.gdgocteambuildingproject.constants.service;

import com.skhu.gdgocteambuildingproject.constants.dto.GenerationResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ConstantsServiceImpl implements ConstantsService {

    @Override
    public List<GenerationResponseDto> getGenerations() {
        return Arrays.stream(Generation.values())
                .sorted(Comparator.comparing(Generation::ordinal))
                .map(GenerationResponseDto::from)
                .toList();
    }
}
