package de.zalando.zally.exception;

import org.zalando.problem.ThrowableProblem;

import javax.ws.rs.core.Response;

public final class MissingApiDefinitionException extends ThrowableProblem {

    public final static String TITLE = "Missing API Definition";
    public final static String DETAIL = "An api definition is missing in api_definition field";

    @Override
    public String getDetail() {
        return MissingApiDefinitionException.DETAIL;
    }

    @Override
    public Response.StatusType getStatus() {
        return Response.Status.BAD_REQUEST;
    }

    @Override
    public String getTitle() {
        return MissingApiDefinitionException.TITLE;
    }
}
