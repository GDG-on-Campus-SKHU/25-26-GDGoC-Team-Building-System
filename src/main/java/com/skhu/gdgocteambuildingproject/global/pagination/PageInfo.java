package com.skhu.gdgocteambuildingproject.global.pagination;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record PageInfo(
        long totalCount,
        int totalPages,
        boolean hasNext
) {
    public static PageInfo from(Page<?> page) {
        return PageInfo.builder()
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .build();
    }
}
