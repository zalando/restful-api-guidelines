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

    public List<JsonObject> getMustViolations() {
        return getViolationsByType("MUST");
    }

    public List<JsonObject> getShouldViolations() {
        return getViolationsByType("SHOULD");
    }

    private List<JsonObject> getViolationsByType(String violationType) {
        return getViolationStream()
                .map(v -> v.asObject())
                .filter(v -> v.getString("violation_type", "").equals(violationType))
                .collect(Collectors.toList());
    }

    private Stream<JsonValue> getViolationStream() throws RuntimeException {


        if (violations == null || violations.get(VIOLATIONS) == null || !violations.get(VIOLATIONS).isArray()) {
            throw new RuntimeException("Response JSON is malformed:" + violations.asString());
        }
        return violations.get("violations").asArray().values().stream();
    }
}
