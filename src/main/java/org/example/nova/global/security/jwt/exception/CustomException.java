package org.example.nova.global.security.jwt.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return errorCode.getStatus();
    }

    @Override
    public String getMessage() {
        return message;
    }

}