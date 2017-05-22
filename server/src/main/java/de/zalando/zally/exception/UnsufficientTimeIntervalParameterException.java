package de.zalando.zally.exception;

public class UnsufficientTimeIntervalParameterException extends RuntimeException {

    public UnsufficientTimeIntervalParameterException() {
        super("TO parameter was supplied without corresponding FROM parameter");
    }
}
