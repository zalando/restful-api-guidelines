package de.zalando.zally.exception;

public final class MissingApiDefinitionException extends RuntimeException {

    public final static String MESSAGE = "An api definition is missing in api_definition field";

    public MissingApiDefinitionException() {
        super(MissingApiDefinitionException.MESSAGE);
    }
}
