package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class NestedPathsCouldBeRootPathsRuleTest {

    @Test
    public void avoidLinkHeadersValidJson() throws IOException {
        NestedPathsCouldBeRootPathsRule rule = new NestedPathsCouldBeRootPathsRule();
        Swagger swagger = new SwaggerParser().read("/api_spp.json");
        List<Violation> violations = rule.validate(swagger);
        assertThat(violations.size()).isEqualTo(1);
        assertTrue(violations.stream().allMatch(v -> ViolationType.COULD.equals(v.getViolationType())));
        assertTrue(violations.stream().allMatch(v -> "Consider Using (Non-) Nested URLs".equals(v.getTitle())));
    }

    @Test
    public void avoidLinkHeadersValidYaml() throws IOException {
        NestedPathsCouldBeRootPathsRule rule = new NestedPathsCouldBeRootPathsRule();
        Swagger swagger = new SwaggerParser().read("/api_spa.yaml");
        assertThat(rule.validate(swagger)).isEmpty();
    }
}
