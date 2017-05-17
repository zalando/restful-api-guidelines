package de.zalando.zally.cli.api;

import static org.junit.Assert.assertEquals;

import de.zalando.zally.cli.domain.Violation;
import de.zalando.zally.cli.domain.ViolationsCount;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;


public class ViolationsApiResponseTest {
    @Test
    public void returnsListOfViolations() {
        final JSONObject firstViolation = new JSONObject();
        firstViolation.put("title", "Test title");
        firstViolation.put("description", "Test description");
        firstViolation.put("violation_type", "MUST");
        firstViolation.put("rule_link", "http://example.com");
        firstViolation.put("paths", new JSONArray());

        final JSONObject secondViolation = new JSONObject();
        secondViolation.put("title", "Test title");
        secondViolation.put("description", "Test description");
        secondViolation.put("violation_type", "MUST");

        final JSONArray violations = new JSONArray();
        violations.put(firstViolation);
        violations.put(secondViolation);

        final JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("violations", violations);

        ViolationsApiResponse apiResponse = new ViolationsApiResponse(jsonResponse);

        List<Violation> result = apiResponse.getViolations();
        assertEquals(2, result.size());
    }

    @Test
    public void returnsMessage() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Test message");

        final ViolationsApiResponse response = new ViolationsApiResponse(jsonObject);

        assertEquals("Test message", response.getMessage());
    }

    @Test
    public void returnsViolationCounters() {
        final JSONObject violationsCount = new JSONObject();
        violationsCount.put("must", 11);
        violationsCount.put("should", 13);
        violationsCount.put("may", 15);
        violationsCount.put("hint", 17);

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("violations_count", violationsCount);

        final ViolationsApiResponse response = new ViolationsApiResponse(jsonObject);
        final ViolationsCount counters = response.getCounters();

        assertEquals(Integer.valueOf(11), counters.get("must"));
        assertEquals(Integer.valueOf(13), counters.get("should"));
        assertEquals(Integer.valueOf(15), counters.get("may"));
        assertEquals(Integer.valueOf(17), counters.get("hint"));
    }
}
