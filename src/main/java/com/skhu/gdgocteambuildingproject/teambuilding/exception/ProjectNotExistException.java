package com.skhu.gdgocteambuildingproject.teambuilding.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ProjectNotExistException extends EntityNotFoundException {
    public ProjectNotExistException(String message) {
        super(message);
    }
}
