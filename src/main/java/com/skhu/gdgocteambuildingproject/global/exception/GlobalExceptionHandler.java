package com.skhu.gdgocteambuildingproject.global.exception;

import com.skhu.gdgocteambuildingproject.admin.exception.ActivityPostNotFoundException;
import com.skhu.gdgocteambuildingproject.admin.exception.AdminUserManageNotExistException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<String> handleNotFound(Exception exception) {
        log.warn("[ERROR RESPONSE] handleNotFound", exception);

        return ResponseEntity.status(NOT_FOUND).body("존재하지 않는 엔드포인트입니다");
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientError(HttpClientErrorException exception) {
        log.warn("[ERROR RESPONSE] HTTP Client exception", exception);

        return ResponseEntity.status(BAD_REQUEST).body("잘못된 값으로 인해 외부 API와 통신에 실패했습니다.");
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<String> handleHttpServerError(HttpServerErrorException exception) {
        log.warn("[ERROR RESPONSE] HTTP Server exception", exception);

        return ResponseEntity.status(BAD_GATEWAY).body("외부 API 불량으로 통신에 실패했습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleBeanValidation(MethodArgumentNotValidException exception) {
        log.warn("[ERROR RESPONSE] bean validation", exception);

        FieldError fieldError = exception.getBindingResult()
                .getFieldErrors()
                .getFirst();

        return ResponseEntity.status(BAD_REQUEST).body(fieldError.getDefaultMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException exception) {
        log.warn("[ERROR RESPONSE] entity not found", exception);

        return ResponseEntity.status(NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException exception) {
        log.warn("[ERROR RESPONSE] illegal argument", exception);

        return ResponseEntity.status(BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException exception) {
        log.warn("[ERROR RESPONSE] illegal state", exception);

        return ResponseEntity.status(BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleIllegalJwt(JwtException exception) {
        log.warn("[ERROR RESPONSE] illegal jwt", exception);

        return ResponseEntity.status(UNAUTHORIZED).body("부적절한 JWT 토큰입니다.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleIllegalJson(HttpMessageNotReadableException exception) {
        log.warn("[ERROR RESPONSE] http message not readable", exception);

        return ResponseEntity.status(BAD_REQUEST).body("JSON 파싱에 실패했습니다."); }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<String> handleLockedException(LockedException exception) {
        log.warn("[ERROR RESPONSE] banned user", exception);

        return ResponseEntity
                .status(FORBIDDEN)
                .body(exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleIllegalHttpMethod(HttpRequestMethodNotSupportedException exception) {
        log.warn("[ERROR RESPONSE] http method not supported", exception);

        return ResponseEntity.status(METHOD_NOT_ALLOWED).body("부적절한 HTTP 메서드입니다.");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleIllegalMediaType(HttpMediaTypeNotSupportedException exception) {
        log.warn("[ERROR RESPONSE] media type not supported", exception);

        return ResponseEntity.status(UNSUPPORTED_MEDIA_TYPE).body("부적절한 Content-Type 혹은 Accept입니다.");
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class})
    public ResponseEntity<String> handleDataIntegrityViolation(Exception exception) {
        log.warn("[ERROR RESPONSE] data integrity violation", exception);

        return ResponseEntity.status(CONFLICT).body("DB 무결성 제약조건에 위반됩니다.");
    }

    @ExceptionHandler({AdminUserManageNotExistException.class, ActivityPostNotFoundException.class})
    public ResponseEntity<String> handleCustomNotFoundException(RuntimeException exception) {
        log.warn("[ERROR RESPONSE] custom not found exception", exception);

        return ResponseEntity.status(NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFound(UsernameNotFoundException exception) {
        log.warn("[ERROR RESPONSE] username not found", exception);

        return ResponseEntity.status(UNAUTHORIZED).body(exception.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatus(ResponseStatusException exception) {
        log.warn("[ERROR RESPONSE] response status exception", exception);

        String reason = exception.getReason();
        String message = reason != null ? reason : exception.getMessage();

        return ResponseEntity.status(exception.getStatusCode()).body(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception exception) {
        log.error("[ERROR RESPONSE] unexpected exception", exception);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("예상하지 못한 예외가 발생했습니다.");
    }
}
