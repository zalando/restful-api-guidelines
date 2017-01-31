package de.zalando.zally.cli;

import static org.junit.Assert.assertEquals;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class ViolationsFilterTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

    @Test
    public void getCouldViolationsReturnsOnlyShould() {
        JsonObject fixtures = getFixtureViolations();
        JsonObject couldViolations = fixtures.get("violations").asArray().get(2).asObject();

        ViolationsFilter violationsFilter = new ViolationsFilter(fixtures);

        List<JsonObject> result = violationsFilter.getCouldViolations();
        assertEquals(1, result.size());
        assertEquals(couldViolations, result.get(0));
    }

    @Test
    public void getMustViolationRaisesCliExceptionWhenViolationsAreNull() {
        assertRaisesCliException("Command-line Parameters: Response JSON is malformed");
        ViolationsFilter violationsFilter = new ViolationsFilter(null);
        violationsFilter.getMustViolations();
    }

    @Test
    public void getShouldViolationRaisesCliExceptionWhenViolationsAreNull() {
        assertRaisesCliException("Command-line Parameters: Response JSON is malformed");
        ViolationsFilter violationsFilter = new ViolationsFilter(null);
        violationsFilter.getShouldViolations();
    }

    @Test
    public void getCouldViolationRaisesCliExceptionWhenViolationsAreNull() {
        assertRaisesCliException("Command-line Parameters: Response JSON is malformed");
        ViolationsFilter violationsFilter = new ViolationsFilter(null);
        violationsFilter.getCouldViolations();
    }

    @Test
    public void getMustViolationRaisesCliExceptionWhenViolationsAreMalformed() {
        assertRaisesCliException("Command-line Parameters: Response JSON is malformed\n\n{\"hello\":\"world\"}");
        JsonObject malformedObject = new JsonObject().add("hello", "world");
        ViolationsFilter violationsFilter = new ViolationsFilter(malformedObject);
        violationsFilter.getMustViolations();
    }

    @Test
    public void getShouldViolationRaisesCliExceptionWhenViolationsAreMalformed() {
        assertRaisesCliException("Command-line Parameters: Response JSON is malformed\n\n{\"hello\":\"world\"}");
        JsonObject malformedObject = new JsonObject().add("hello", "world");
        ViolationsFilter violationsFilter = new ViolationsFilter(malformedObject);
        violationsFilter.getShouldViolations();
    }

    @Test
    public void getCouldViolationRaisesCliExceptionWhenViolationsAreMalformed() {
        assertRaisesCliException("Command-line Parameters: Response JSON is malformed\n\n{\"hello\":\"world\"}");
        JsonObject malformedObject = new JsonObject().add("hello", "world");
        ViolationsFilter violationsFilter = new ViolationsFilter(malformedObject);
        violationsFilter.getCouldViolations();
    }

    private void assertRaisesCliException(String expectedError) {
        expectedException.expect(CliException.class);
        expectedException.expectMessage(expectedError);
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

        JsonObject couldViolation = new JsonObject();
        couldViolation.add("title", "Test should");
        couldViolation.add("description", "Test should");
        couldViolation.add("violation_type", "COULD");

        JsonArray violations = new JsonArray();
        violations.add(mustViolation);
        violations.add(shouldViolation);
        violations.add(couldViolation);

        JsonObject result = new JsonObject();
        result.add("violations", violations);

        return result;
    }

}
