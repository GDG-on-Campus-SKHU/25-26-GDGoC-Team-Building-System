package com.skhu.gdgocteambuildingproject.constants.controller;

import com.skhu.gdgocteambuildingproject.constants.dto.GenerationResponseDto;
import com.skhu.gdgocteambuildingproject.constants.dto.PartResponseDto;
import com.skhu.gdgocteambuildingproject.constants.api.ConstantsApi;
import com.skhu.gdgocteambuildingproject.constants.service.ConstantsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/constants")
public class ConstantsController implements ConstantsApi {

    private final ConstantsService constantsService;

    @Override
    @GetMapping("/generations")
    public ResponseEntity<List<GenerationResponseDto>> getGenerations() {
        List<GenerationResponseDto> response = constantsService.getGenerations();

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/parts")
    public ResponseEntity<List<PartResponseDto>> getParts() {
        List<PartResponseDto> response = constantsService.getParts();

        return ResponseEntity.ok(response);
    }
}
