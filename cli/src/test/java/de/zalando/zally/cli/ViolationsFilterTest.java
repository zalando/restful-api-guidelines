package de.zalando.zally.cli;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ViolationsFilterTest {
    @Test
    public void formatReturnsProperlyFormattedString() {
        JsonObject violation = new JsonObject();
        violation.add("title", "Test title");
        violation.add("description", "Test description");

        String result = ViolationsFilter.formatViolation(violation);

        assertEquals("Test title:\n\tTest description", result);
    }

    @Test
    public void getMustViolationsReturnsOnlyMust() {
        JsonObject fixtures = getFixtureViolations();
        JsonObject mustViolation = fixtures.get("violations").asArray().get(0).asObject();

        ViolationsFilter violationsFilter = new ViolationsFilter(fixtures);

        List<String> result = violationsFilter.getMustViolations();
        assertEquals(1, result.size());
        assertEquals(violationsFilter.formatViolation(mustViolation), result.get(0));
    }

    @Test
    public void getShouldViolationsReturnsOnlyShould() {
        JsonObject fixtures = getFixtureViolations();
        JsonObject shouldViolation = fixtures.get("violations").asArray().get(1).asObject();

        ViolationsFilter violationsFilter = new ViolationsFilter(fixtures);

        List<String> result = violationsFilter.getShouldViolations();
        assertEquals(1, result.size());
        assertEquals(violationsFilter.formatViolation(shouldViolation), result.get(0));
    }

    private JsonObject getFixtureViolations() {
        JsonObject result = new JsonObject();
        JsonArray violations = new JsonArray();

        JsonObject mustViolation = new JsonObject();
        mustViolation.add("title", "Test must");
        mustViolation.add("description", "Test must");
        mustViolation.add("violationType", "MUST");

        JsonObject shouldViolation = new JsonObject();
        shouldViolation.add("title", "Test should");
        shouldViolation.add("description", "Test should");
        shouldViolation.add("violationType", "SHOULD");

        violations.add(mustViolation);
        violations.add(shouldViolation);

        result.add("violations", violations);

        return result;
    }
}
