package de.zalando.zally.exception;

import org.springframework.http.HttpStatus;

public class UnaccessibleResourceUrlException extends RuntimeException {
    private final HttpStatus httpStatus;

    public UnaccessibleResourceUrlException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
