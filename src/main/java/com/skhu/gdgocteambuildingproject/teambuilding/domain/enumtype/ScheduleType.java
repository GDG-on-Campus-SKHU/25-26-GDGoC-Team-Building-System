package com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype;

import lombok.Getter;

@Getter
public enum ScheduleType {
    IDEA_REGISTRATION(false, true),
    FIRST_TEAM_BUILDING(true, true),
    FIRST_TEAM_BUILDING_ANNOUNCEMENT(false, false),
    SECOND_TEAM_BUILDING(true, false),
    SECOND_TEAM_BUILDING_ANNOUNCEMENT(false, false),
    THIRD_TEAM_BUILDING(false, false),
    FINAL_RESULT_ANNOUNCEMENT(false, false);

    private final boolean enrollmentAvailable;
    private final boolean ideaDeletable;

    ScheduleType(
            boolean enrollmentAvailable,
            boolean ideaDeletable
    ) {
        this.enrollmentAvailable = enrollmentAvailable;
        this.ideaDeletable = ideaDeletable;
    }
}
