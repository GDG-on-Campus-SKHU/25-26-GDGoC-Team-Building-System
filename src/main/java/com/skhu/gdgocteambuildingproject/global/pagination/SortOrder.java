package com.skhu.gdgocteambuildingproject.global.pagination;

import java.util.function.Function;
import org.springframework.data.domain.Sort;

public enum SortOrder {
    ASC((sortBy) -> Sort.by(sortBy).ascending()),
    DESC((sortBy) -> Sort.by(sortBy).descending());

    private final Function<String, Sort> sortFunction;

    SortOrder(Function<String, Sort> sortFunction) {
        this.sortFunction = sortFunction;
    }

    public Sort sort(String sortBy) {
        return sortFunction.apply(sortBy);
    }
}
