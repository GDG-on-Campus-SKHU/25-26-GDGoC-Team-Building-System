package com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype;

import lombok.Getter;

@Getter
public enum EnrollmentStatus {
    WAITING(true),
    EXPIRED(false),
    REJECTED(false),
    SCHEDULED_TO_REJECT(true),
    ACCEPTED(false),
    SCHEDULED_TO_ACCEPT(true);

    private final boolean waitingToConfirm;

    EnrollmentStatus(boolean waitingToConfirm) {
        this.waitingToConfirm = waitingToConfirm;
    }

    public EnrollmentStatus confirmedStatus() {
        return switch (this) {
            case SCHEDULED_TO_ACCEPT -> ACCEPTED;
            case SCHEDULED_TO_REJECT -> REJECTED;
            default -> this;
        };
    }
}
