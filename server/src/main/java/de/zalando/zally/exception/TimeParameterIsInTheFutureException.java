package de.zalando.zally.exception;

public class TimeParameterIsInTheFutureException extends RuntimeException {

    public TimeParameterIsInTheFutureException() {
        super("Time parameter is in the future");
    }
}
