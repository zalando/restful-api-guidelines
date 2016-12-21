package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;

import java.util.*;
import java.util.stream.Collectors;

public abstract class HttpHeadersRule implements Rule {

    public static final Set<String> PARAMETER_NAMES_WHITELIST = new HashSet<>(Arrays.asList("ETag", "TSV", "TE",
            "Content-MD5", "DNT", "X-ATT-DeviceId", "X-UIDH", "X-Request-ID", "X-Correlation-ID", "WWW-Authenticate",
            "X-XSS-Protection", "X-Flow-ID", "X-UID", "X-Tenant-ID", "X-Device-OS"));

    abstract Violation createViolation(String header, Optional<String> path);

    abstract boolean isViolation(String header);

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> res = new ArrayList<>();
        if (swagger.getParameters() != null) {
            res.addAll(validateParameters(swagger.getParameters().values(), Optional.empty()));
        }
        if (swagger.getPaths() != null) {
            for (Map.Entry<String, Path> pathEntry: swagger.getPaths().entrySet()) {
                Optional<String> pathName = Optional.of(pathEntry.getKey());
                res.addAll(validateParameters(pathEntry.getValue().getParameters(), pathName));
                for (Operation operation : pathEntry.getValue().getOperations()) {
                    res.addAll(validateParameters(operation.getParameters(), pathName));
                    res.addAll(validateHeaders(getResponseHeaders(operation.getResponses()), pathName));
                }
            }
        }
        res.addAll(validateHeaders(getResponseHeaders(swagger.getResponses()), Optional.empty()));
        return res;
    }

    private List<Violation> validateParameters(Collection<Parameter> parameters, Optional<String> path) {
        if (parameters == null) {
            return Collections.emptyList();
        }
        return validateHeaders(parameters
                .stream()
                .filter(p -> p.getIn().equals("header"))
                .map(Parameter::getName)
                .collect(Collectors.toList()), path);
    }

    private List<Violation> validateHeaders(Collection<String> headers, Optional<String> path) {
        if (headers == null) {
            return Collections.emptyList();
        }
        return headers
                .stream()
                .filter(p -> !PARAMETER_NAMES_WHITELIST.contains(p) && isViolation(p))
                .map(p -> this.createViolation(p, path))
                .collect(Collectors.toList());
    }

    private Set<String> getResponseHeaders(Map<String, Response> responses) {
        if (responses != null) {
            for (Response response : responses.values()) {
                if (response.getHeaders() != null) {
                    return response.getHeaders().keySet();
                }
            }
        }
        return Collections.emptySet();
    }
}
