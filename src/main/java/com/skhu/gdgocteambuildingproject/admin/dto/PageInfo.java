package com.skhu.gdgocteambuildingproject.admin.dto;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record PageInfo(
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
    public static PageInfo from(Page<?> page) {
        return PageInfo.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}