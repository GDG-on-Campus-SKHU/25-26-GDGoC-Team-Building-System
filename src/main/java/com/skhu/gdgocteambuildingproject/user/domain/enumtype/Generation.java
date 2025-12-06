package com.skhu.gdgocteambuildingproject.user.domain.enumtype;

import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;

import java.util.Arrays;

public enum Generation {
    GEN_22_23("22-23"),
    GEN_23_24("23-24"),
    GEN_24_25("24-25"),
    GEN_25_26("25-26");

    private final String label;

    Generation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Generation fromLabel(String label) {
        return Arrays.stream(values())
                .filter(gen -> gen.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.INVALID_GENERATION.getMessage()));
    }
}
