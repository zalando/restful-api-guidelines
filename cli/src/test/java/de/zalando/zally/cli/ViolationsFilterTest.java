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

        List<JsonObject> result = violationsFilter.getViolations("MUST");
        assertEquals(1, result.size());
        assertEquals(mustViolation, result.get(0));
    }

    @Test
    public void getShouldViolationsReturnsOnlyShould() {
        JsonObject fixtures = getFixtureViolations();
        JsonObject shouldViolation = fixtures.get("violations").asArray().get(1).asObject();

        ViolationsFilter violationsFilter = new ViolationsFilter(fixtures);

        List<JsonObject> result = violationsFilter.getViolations("SHOULD");
        assertEquals(1, result.size());
        assertEquals(shouldViolation, result.get(0));
    }

    @Test
    public void getCouldViolationsReturnsOnlyShould() {
        JsonObject fixtures = getFixtureViolations();
        JsonObject couldViolations = fixtures.get("violations").asArray().get(2).asObject();

        ViolationsFilter violationsFilter = new ViolationsFilter(fixtures);

        List<JsonObject> result = violationsFilter.getViolations("COULD");
        assertEquals(1, result.size());
        assertEquals(couldViolations, result.get(0));
    }

    @Test
    public void getMustViolationRaisesCliExceptionWhenViolationsAreNull() {
        assertRaisesCliException("API: Response JSON is malformed");
        ViolationsFilter violationsFilter = new ViolationsFilter(null);
        violationsFilter.getViolations("MUST");
    }

    @Test
    public void getShouldViolationRaisesCliExceptionWhenViolationsAreNull() {
        assertRaisesCliException("API: Response JSON is malformed");
        ViolationsFilter violationsFilter = new ViolationsFilter(null);
        violationsFilter.getViolations("SHOULD");
    }

    @Test
    public void getCouldViolationRaisesCliExceptionWhenViolationsAreNull() {
        assertRaisesCliException("API: Response JSON is malformed");
        ViolationsFilter violationsFilter = new ViolationsFilter(null);
        violationsFilter.getViolations("COULD");
    }

    @Test
    public void getHintViolationRaisesCliExceptionWhenViolationsAreNull() {
        assertRaisesCliException("API: Response JSON is malformed");
        ViolationsFilter violationsFilter = new ViolationsFilter(null);
        violationsFilter.getViolations("HINT");
    }

    @Test
    public void getMustViolationRaisesCliExceptionWhenViolationsAreMalformed() {
        assertRaisesCliException("API: Response JSON is malformed\n\n{\"hello\":\"world\"}");
        JsonObject malformedObject = new JsonObject().add("hello", "world");
        ViolationsFilter violationsFilter = new ViolationsFilter(malformedObject);
        violationsFilter.getViolations("MUST");
    }

    @Test
    public void getShouldViolationRaisesCliExceptionWhenViolationsAreMalformed() {
        assertRaisesCliException("API: Response JSON is malformed\n\n{\"hello\":\"world\"}");
        JsonObject malformedObject = new JsonObject().add("hello", "world");
        ViolationsFilter violationsFilter = new ViolationsFilter(malformedObject);
        violationsFilter.getViolations("SHOULD");
    }

    @Test
    public void getCouldViolationRaisesCliExceptionWhenViolationsAreMalformed() {
        assertRaisesCliException("API: Response JSON is malformed\n\n{\"hello\":\"world\"}");
        JsonObject malformedObject = new JsonObject().add("hello", "world");
        ViolationsFilter violationsFilter = new ViolationsFilter(malformedObject);
        violationsFilter.getViolations("COULD");
    }

    @Test
    public void getHintViolationRaisesCliExceptionWhenViolationsAreMalformed() {
        assertRaisesCliException("API: Response JSON is malformed\n\n{\"hello\":\"world\"}");
        JsonObject malformedObject = new JsonObject().add("hello", "world");
        ViolationsFilter violationsFilter = new ViolationsFilter(malformedObject);
        violationsFilter.getViolations("HINT");
    }

    private void assertRaisesCliException(String expectedError) {
        expectedException.expect(CliException.class);
        expectedException.expectMessage(expectedError);
    }

    private JsonObject getFixtureViolations() {
        final JsonObject mustViolation = new JsonObject();
        mustViolation.add("title", "Test must");
        mustViolation.add("description", "Test must");
        mustViolation.add("violation_type", "MUST");

        final JsonObject shouldViolation = new JsonObject();
        shouldViolation.add("title", "Test should");
        shouldViolation.add("description", "Test should");
        shouldViolation.add("violation_type", "SHOULD");

        final JsonObject couldViolation = new JsonObject();
        couldViolation.add("title", "Test could");
        couldViolation.add("description", "Test could");
        couldViolation.add("violation_type", "COULD");

        final JsonObject hintViolation = new JsonObject();
        hintViolation.add("title", "Test hint");
        hintViolation.add("description", "Test hint");
        hintViolation.add("violation_type", "HINT");

        final JsonArray violations = new JsonArray();
        violations.add(mustViolation);
        violations.add(shouldViolation);
        violations.add(couldViolation);
        violations.add(hintViolation);

        JsonObject result = new JsonObject();
        result.add("violations", violations);

        return result;
    }

}
