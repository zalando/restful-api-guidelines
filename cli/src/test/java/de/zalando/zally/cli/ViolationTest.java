package de.zalando.zally.cli;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ViolationTest {
    @Test
    public void createsViolationObjectFromJson() throws Exception {
        final Violation violation = new Violation(getViolationJson());
        final List<String> expectedRules = new ArrayList<>();
        expectedRules.add("path1");
        expectedRules.add("path2");

        assertEquals("Test", violation.getTitle());
        assertEquals("Test Description", violation.getDescription());
        assertEquals("MUST", violation.getViolationType());
        assertEquals("http://example.com", violation.getRuleLink());
        assertEquals(expectedRules, violation.getPaths());
    }

    private JsonObject getViolationJson() {
        final JsonArray paths = new JsonArray();
        paths.add("path1");
        paths.add("path2");

        final JsonObject jsonViolation = new JsonObject();
        jsonViolation.set("title", "Test");
        jsonViolation.set("description", "Test Description");
        jsonViolation.set("violation_type", "MUST");
        jsonViolation.set("rule_link", "http://example.com");
        jsonViolation.set("paths", paths);

        return jsonViolation;
    }
}
