package com.skhu.gdgocteambuildingproject.Idea.domain.enumtype;

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
}
