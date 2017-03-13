package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

public class ZallyApiResponse {
    private final JsonObject response;

    private static final String VIOLATIONS = "violations";
    private static final String MESSAGE = "message";
    private static final String VIOLATIONS_COUNT = "violations_count";

    public ZallyApiResponse(JsonObject response) {
        this.response = response;
    }

    public List<Violation> getViolations() throws CliException {
        if (response == null || response.get(VIOLATIONS) == null || !response.get(VIOLATIONS).isArray()) {
            String violationsString = response == null ? null : response.toString();
            throw new CliException(CliExceptionType.API, "Response JSON is malformed", violationsString);
        }
        return response
                .get(VIOLATIONS)
                .asArray()
                .values()
                .stream()
                .map(v -> new Violation(v.asObject()))
                .collect(Collectors.toList());
    }

    public String getMessage() {
        if (response == null || !response.names().contains(MESSAGE)) {
            return null;
        }
        return response.get(MESSAGE).asString();
    }

    public ViolationsCount getCounters() {
        if (response == null || response.get(VIOLATIONS_COUNT) == null || !response.get(VIOLATIONS_COUNT).isObject()) {
            throw new CliException(CliExceptionType.API, "Respones does not contain violation counters");
        }
        return new ViolationsCount(response.get(VIOLATIONS_COUNT).asObject());
    }
}
