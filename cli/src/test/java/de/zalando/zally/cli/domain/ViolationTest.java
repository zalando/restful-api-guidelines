package de.zalando.zally.cli.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;


public class ViolationTest {
    @Test
    public void createsViolationObjectFromJson() throws Exception {
        final Violation violation = new Violation(getViolationJson());
        final List<String> expectedRules = new ArrayList<>();
        expectedRules.add("path1");
        expectedRules.add("path2");

        assertEquals("Test", violation.getTitle());
        assertEquals("Test Description", violation.getDescription());
        assertEquals(ViolationType.MUST, violation.getViolationType());
        assertEquals("http://example.com", violation.getRuleLink());
        assertEquals(expectedRules, violation.getPaths());
    }

    @Test
    public void createsViolationWithOnlyTitleAndDescriptionSpecified() throws Exception {
        final JSONObject jsonViolation = new JSONObject();
        jsonViolation.put("title", "Test");
        jsonViolation.put("description", "Test Description");
        jsonViolation.put("violation_type", "MUST");

        final Violation violation = new Violation(jsonViolation);

        assertEquals("Test", violation.getTitle());
        assertEquals("Test Description", violation.getDescription());
        assertEquals(ViolationType.MUST, violation.getViolationType());
        assertNull(violation.getRuleLink());
        assertEquals(new ArrayList<>(), violation.getPaths());
    }

    @Test
    public void createsViolationUsingAlternativeConstructor() throws Exception {
        final Violation violation = new Violation("Test", "Test Description");

        assertEquals("Test", violation.getTitle());
        assertEquals("Test Description", violation.getDescription());
        assertNull(violation.getRuleLink());
        assertNull(violation.getViolationType());
        assertEquals(new ArrayList<>(), violation.getPaths());

    }

    private JSONObject getViolationJson() {
        final JSONArray paths = new JSONArray();
        paths.put("path1");
        paths.put("path2");

        final JSONObject jsonViolation = new JSONObject();
        jsonViolation.put("title", "Test");
        jsonViolation.put("description", "Test Description");
        jsonViolation.put("violation_type", "MUST");
        jsonViolation.put("rule_link", "http://example.com");
        jsonViolation.put("paths", paths);

        return jsonViolation;
    }
}
