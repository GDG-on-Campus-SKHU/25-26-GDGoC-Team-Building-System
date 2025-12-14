package com.skhu.gdgocteambuildingproject.constants.service;

import com.skhu.gdgocteambuildingproject.constants.dto.GenerationResponseDto;
import com.skhu.gdgocteambuildingproject.constants.dto.PartResponseDto;
import java.util.List;

public interface ConstantsService {
    List<GenerationResponseDto> getGenerations();

    List<PartResponseDto> getParts();
}
