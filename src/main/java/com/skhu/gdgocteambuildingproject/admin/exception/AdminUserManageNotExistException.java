package com.skhu.gdgocteambuildingproject.admin.exception;

import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AdminUserManageNotExistException extends RuntimeException {
    private final ExceptionMessage errorMessage;

    public AdminUserManageNotExistException(ExceptionMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
