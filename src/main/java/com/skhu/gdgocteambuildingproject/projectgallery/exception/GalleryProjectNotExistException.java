package com.skhu.gdgocteambuildingproject.projectgallery.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class GalleryProjectNotExistException extends EntityNotFoundException {
    public GalleryProjectNotExistException(String message) {
        super(message);
    }
}
