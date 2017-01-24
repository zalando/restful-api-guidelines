package de.zalando.zally.cli;

import static org.junit.Assert.assertEquals;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import java.util.List;
import org.junit.Test;


public class ViolationsFilterTest {
    @Test
    public void getMustViolationsReturnsOnlyMust() {
        JsonObject fixtures = getFixtureViolations();
        JsonObject mustViolation = fixtures.get("violations").asArray().get(0).asObject();

        ViolationsFilter violationsFilter = new ViolationsFilter(fixtures);

        List<JsonObject> result = violationsFilter.getMustViolations();
        assertEquals(1, result.size());
        assertEquals(mustViolation, result.get(0));
    }

    @Test
    public void getShouldViolationsReturnsOnlyShould() {
        JsonObject fixtures = getFixtureViolations();
        JsonObject shouldViolation = fixtures.get("violations").asArray().get(1).asObject();

        ViolationsFilter violationsFilter = new ViolationsFilter(fixtures);

        List<JsonObject> result = violationsFilter.getShouldViolations();
        assertEquals(1, result.size());
        assertEquals(shouldViolation, result.get(0));
    }

    private JsonObject getFixtureViolations() {
        JsonObject mustViolation = new JsonObject();
        mustViolation.add("title", "Test must");
        mustViolation.add("description", "Test must");
        mustViolation.add("violation_type", "MUST");

        JsonObject shouldViolation = new JsonObject();
        shouldViolation.add("title", "Test should");
        shouldViolation.add("description", "Test should");
        shouldViolation.add("violation_type", "SHOULD");

        JsonArray violations = new JsonArray();
        violations.add(mustViolation);
        violations.add(shouldViolation);

        JsonObject result = new JsonObject();
        result.add("violations", violations);

        return result;
    }
}
