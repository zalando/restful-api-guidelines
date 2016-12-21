package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class LimitNumberOfSubresourcesRuleTest {

    @Test
    public void shouldReturnViolationIfReachLimit() {
        Swagger swagger = getFixture("fixtures/limitNumberOfSubresourcesInvalid.json");
        Rule rule = new LimitNumberOfSubresourcesRule();
        List<Violation> violations = rule.validate(swagger);
        assertEquals(1, violations.size());
    }

    @Test
    public void shouldNotReturnViolations() {
        Swagger swagger = getFixture("fixtures/limitNumberOfSubresourcesValid.json");
        Rule rule = new LimitNumberOfSubresourcesRule();
        List<Violation> violations = rule.validate(swagger);
        assertEquals(0, violations.size());
    }

    private Swagger getFixture(String fixture) {
        return new SwaggerParser().read(fixture);
    }
}
