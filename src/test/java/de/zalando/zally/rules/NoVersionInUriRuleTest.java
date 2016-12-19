package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class NoVersionInUriRuleTest {
    private final NoVersionInUriRule rule = new NoVersionInUriRule();

    private void assertViolationOccurs(String basePath) {
        Swagger swagger = new Swagger();
        swagger.basePath(basePath);

        List<Violation> violations = rule.validate(swagger);
        assertEquals(1, violations.size());

        Violation violation = violations.get(0);
        assertEquals("Do Not Use URI Versioning", violation.getTitle());
        assertEquals("basePath attribute contains version number", violation.getDescription());
        assertEquals(ViolationType.MUST, violation.getViolationType());
        assertEquals(rule.LINK, violation.getRuleLink());
    }

    @Test
    public void returnsViolationsWhenVersionIsInTheBeginingOfBasePath() {
        assertViolationOccurs("/v1/tests");
    }

    @Test
    public void returnsViolationsWhenVersionIsInTheMiddleOfBasePath() {
        assertViolationOccurs("/api/v1/tests");
    }

    @Test
    public void returnsViolationsWhenVersionIsInTheEndOfBasePath() {
        assertViolationOccurs("/api/v1");
    }

    @Test
    public void returnsViolationsWhenVersionIsBig() {
        assertViolationOccurs("/v1024/tests");
    }

    @Test
    public void returnsEmptyViolationListWhenNoVersionFoundInURL() {
        Swagger swagger = new Swagger();
        swagger.basePath("/violations/");

        List<Violation> violations = rule.validate(swagger);
        assertEquals(0, violations.size());
    }
}
