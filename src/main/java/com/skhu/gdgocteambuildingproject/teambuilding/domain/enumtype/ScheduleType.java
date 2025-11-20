package com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype;

import lombok.Getter;

@Getter
public enum ScheduleType {
    IDEA_REGISTRATION(false),
    FIRST_TEAM_BUILDING(true),
    FIRST_TEAM_BUILDING_ANNOUNCEMENT(false),
    SECOND_TEAM_BUILDING(true),
    SECOND_TEAM_BUILDING_ANNOUNCEMENT(false),
    THIRD_TEAM_BUILDING(false),
    FINAL_RESULT_ANNOUNCEMENT(false);

    private final boolean enrollmentAvailable;

    ScheduleType(boolean enrollmentAvailable) {
        this.enrollmentAvailable = enrollmentAvailable;
    }
}
