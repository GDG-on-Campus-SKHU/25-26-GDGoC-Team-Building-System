package com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype;

import java.util.Comparator;

public enum Choice {
    FIRST, SECOND, THIRD;

    public static Comparator<Choice> comparator() {
        return Comparator.comparing(Choice::ordinal);
    }
}
