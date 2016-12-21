package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;

import java.util.*;
import java.util.stream.Collectors;

public class HyphenateHttpHeadersRule implements Rule {

    public static final String RULE_NAME = "Must: Use Hyphenated HTTP Headers";
    public static final String RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-use-hyphenated-http-headers";
    public static final String DESC_PATTERN = "Header name '%s' is not hyphenated";
    public static final Set<String> PARAMETER_NAMES_WHITELIST = new HashSet<>(Arrays.asList("ETag", "TSV", "TE",
            "Content-MD5", "DNT", "X-ATT-DeviceId", "X-UIDH", "X-Request-ID", "X-Correlation-ID", "WWW-Authenticate",
            "X-XSS-Protection"));

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
                .filter(p -> !PARAMETER_NAMES_WHITELIST.contains(p) && !PatternUtil.isHyphenated(p))
                .map(p -> this.createViolation(p, path))
                .collect(Collectors.toList());
    }

    private Violation createViolation(String header, Optional<String> path) {
        if (path.isPresent()) {
            return new Violation(RULE_NAME, String.format(DESC_PATTERN, header), ViolationType.MUST, RULE_URL, path.get());
        }
        return new Violation(RULE_NAME, String.format(DESC_PATTERN, header), ViolationType.MUST, RULE_URL);
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
