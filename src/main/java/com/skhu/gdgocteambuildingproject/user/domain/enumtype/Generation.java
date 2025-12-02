package com.skhu.gdgocteambuildingproject.user.domain.enumtype;

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
}
