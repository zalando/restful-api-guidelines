package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ViolationsFilter {

    private final JsonObject violations;
    private static final String VIOLATIONS = "violations";

    public ViolationsFilter(JsonObject violations) {
        this.violations = violations;
    }

    public List<JsonObject> getMustViolations() throws CliException {
        return getViolationsByType("MUST");
    }

    public List<JsonObject> getShouldViolations() throws CliException {
        return getViolationsByType("SHOULD");
    }

    public List<JsonObject> getCouldViolations() throws CliException {
        return getViolationsByType("COULD");
    }

    private List<JsonObject> getViolationsByType(String violationType) throws CliException {
        return getViolationStream()
                .map(v -> v.asObject())
                .filter(v -> v.getString("violation_type", "").equalsIgnoreCase(violationType))
                .collect(Collectors.toList());
    }

    private Stream<JsonValue> getViolationStream() throws CliException {
        if (violations == null || violations.get(VIOLATIONS) == null || !violations.get(VIOLATIONS).isArray()) {
            String violationsString = violations == null ? null : violations.toString();
            throw new CliException(CliExceptionType.API, "Response JSON is malformed", violationsString);
        }
        return violations.get("violations").asArray().values().stream();
    }
}
