package com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import lombok.Getter;

@Getter
public enum ScheduleType {
    IDEA_REGISTRATION(
            false,
            true,
            false,
            () -> null
    ),
    FIRST_TEAM_BUILDING(
            true,
            true,
            false,
            () -> IDEA_REGISTRATION
    ),
    FIRST_TEAM_BUILDING_ANNOUNCEMENT(
            false,
            false,
            true,
            () -> FIRST_TEAM_BUILDING
    ),
    SECOND_TEAM_BUILDING(
            true,
            false,
            false,
            () -> FIRST_TEAM_BUILDING_ANNOUNCEMENT
    ),
    SECOND_TEAM_BUILDING_ANNOUNCEMENT(
            false,
            false,
            true,
            () -> SECOND_TEAM_BUILDING
    ),
    THIRD_TEAM_BUILDING(
            false,
            false,
            false,
            () -> SECOND_TEAM_BUILDING_ANNOUNCEMENT
    ),
    FINAL_RESULT_ANNOUNCEMENT(
            false,
            false,
            true,
            () -> THIRD_TEAM_BUILDING
    );

    private final boolean enrollmentAvailable;
    private final boolean ideaDeletable;
    private final boolean announcement;
    private final Supplier<ScheduleType> prevScheduleType;

    ScheduleType(
            boolean enrollmentAvailable,
            boolean ideaDeletable,
            boolean announcement,
            Supplier<ScheduleType> prevScheduleType
    ) {
        this.enrollmentAvailable = enrollmentAvailable;
        this.ideaDeletable = ideaDeletable;
        this.announcement = announcement;
        this.prevScheduleType = prevScheduleType;
    }

    public static List<ScheduleType> enrollments() {
        return Arrays.stream(values())
                .filter(ScheduleType::isEnrollmentAvailable)
                .toList();
    }

    public ScheduleType getPrevScheduleType() {
        return prevScheduleType.get();
    }

    public ScheduleType getNextScheduleType() {
        return switch (this) {
            case IDEA_REGISTRATION -> FIRST_TEAM_BUILDING;
            case FIRST_TEAM_BUILDING -> FIRST_TEAM_BUILDING_ANNOUNCEMENT;
            case FIRST_TEAM_BUILDING_ANNOUNCEMENT -> SECOND_TEAM_BUILDING;
            case SECOND_TEAM_BUILDING -> SECOND_TEAM_BUILDING_ANNOUNCEMENT;
            case SECOND_TEAM_BUILDING_ANNOUNCEMENT -> THIRD_TEAM_BUILDING;
            case THIRD_TEAM_BUILDING -> FINAL_RESULT_ANNOUNCEMENT;
            case FINAL_RESULT_ANNOUNCEMENT -> null;
        };
    }
}
