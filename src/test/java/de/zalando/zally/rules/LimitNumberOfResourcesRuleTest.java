package de.zalando.zally.rules;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for limiting number of resources rule.
 */
public class LimitNumberOfResourcesRuleTest {

    private Swagger validSwagger = getFixture("fixtures/limitNumberOfResourcesValid.json");
    private Swagger invalidSwagger = getFixture("fixtures/limitNumberOfResourcesInvalid.json");

    @Test
    public void shouldReturnNoViolations() {
        assertThat(new LimitNumberOfResourcesRule().validate(validSwagger)).isEmpty();
    }

    @Test
    public void shouldReturnViolations() {
        assertThat(new LimitNumberOfResourcesRule().validate(invalidSwagger)).isNotEmpty();
    }

    private Swagger getFixture(String fixture) {
        return new SwaggerParser().read(fixture);
    }
}
