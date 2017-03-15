package de.zalando.zally.cli.api;

import de.zalando.zally.cli.domain.Violation;
import de.zalando.zally.cli.domain.ViolationsCount;
import de.zalando.zally.cli.exceptions.CliException;
import de.zalando.zally.cli.exceptions.CliExceptionType;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;


public class ZallyApiResponse {
    private final JSONObject response;

    private static final String VIOLATIONS = "violations";
    private static final String MESSAGE = "message";
    private static final String VIOLATIONS_COUNT = "violations_count";

    public ZallyApiResponse(JSONObject response) {
        this.response = response;
    }

    public List<Violation> getViolations() throws CliException {
        if (!isResponseContainJsonArray(VIOLATIONS)) {
            String violationsString = response == null ? null : response.toString();
            throw new CliException(CliExceptionType.API, "Response JSON is malformed", violationsString);
        }

        final List<Violation> violations = new ArrayList<>();
        final JSONArray jsonViolations = response.getJSONArray(VIOLATIONS);
        for (int i = 0; i < jsonViolations.length(); i++) {
            violations.add(new Violation((JSONObject) jsonViolations.get(i)));
        }

        return violations;
    }

    public String getMessage() {
        if (response == null || !response.has(MESSAGE)) {
            return null;
        }
        return response.getString(MESSAGE);
    }

    public ViolationsCount getCounters() {
        if (!isResponseContainJsonObject(VIOLATIONS_COUNT)) {
            throw new CliException(CliExceptionType.API, "Respones does not contain violation counters");
        }
        return new ViolationsCount((JSONObject) response.get(VIOLATIONS_COUNT));
    }

    private Boolean isResponseContainJsonArray(final String name) {
        return response != null && response.get(name) != null && response.get(name) instanceof JSONArray;
    }

    private Boolean isResponseContainJsonObject(final String name) {
        return response != null && response.get(name) != null && response.get(name) instanceof JSONObject;
    }
}
