package de.zalando.zally.cli.api;

import static org.junit.Assert.assertEquals;

import de.zalando.zally.cli.domain.Violation;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;


public class ZallyApiResponseTest {
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

        final JSONArray violations = new JSONArray();
        violations.put(firstViolation);
        violations.put(secondViolation);

        final JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("violations", violations);

        ZallyApiResponse apiResponse = new ZallyApiResponse(jsonResponse);

        List<Violation> result = apiResponse.getViolations();
        assertEquals(2, result.size());
    }

}