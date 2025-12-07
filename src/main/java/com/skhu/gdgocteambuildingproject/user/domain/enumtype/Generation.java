package com.skhu.gdgocteambuildingproject.user.domain.enumtype;

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
                .filter(generation -> generation.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 기수입니다: " + label));
    }
}
