package de.zalando.zally.cli;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ZallyApiResponseTest {
    @Test
    public void returnsListOfViolations() {
        final JsonArray violations = new JsonArray();
        final JsonObject firstViolation = new JsonObject();
        firstViolation.add("title", "Test title");
        firstViolation.add("description", "Test description");
        firstViolation.add("violation_type", "MUST");
        firstViolation.add("rule_link", "http://example.com");
        firstViolation.add("paths", Json.array());

        final JsonObject secondViolation = new JsonObject();
        secondViolation.add("title", "Test title");
        secondViolation.add("description", "Test description");

        violations.add(firstViolation);
        violations.add(secondViolation);

        final JsonObject jsonResponse = new JsonObject();
        jsonResponse.add("violations", violations);

        ZallyApiResponse apiResponse = new ZallyApiResponse(jsonResponse);

        List<Violation> result = apiResponse.getViolations();
        assertEquals(2, result.size());
    }

}