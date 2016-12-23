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

public class Use429HeaderForRateLimitRuleTest {

    @Test
    public void responseUse429HeaderForRateLimitRuleNoViolationsJson() throws IOException {
        Use429HeaderForRateLimitRule rule = new Use429HeaderForRateLimitRule();
        Swagger swagger = new SwaggerParser().read("/fixtures/use429HeadersForRateLimitValid.json");
        assertThat(rule.validate(swagger)).isEmpty();
    }

    @Test
    public void responseUse429HeaderForRateLimitRuleViolationsJson() throws IOException {
        Use429HeaderForRateLimitRule rule = new Use429HeaderForRateLimitRule();
        Swagger swagger = new SwaggerParser().read("/fixtures/use429HeadersForRateLimitInvalidHeader.json");
        List<Violation> violations = rule.validate(swagger);
        assertThat(violations.size()).isEqualTo(3);
        assertTrue(violations.stream().allMatch(v -> ViolationType.MUST.equals(v.getViolationType())));
        assertTrue(violations.stream().allMatch(v -> "Use 429 With Header For Rate Limits".equals(v.getTitle())));
    }

    @Test
    public void responseUse429HeaderForRateLimitRuleNoViolationYaml() throws IOException {
        Use429HeaderForRateLimitRule rule = new Use429HeaderForRateLimitRule();
        Swagger swagger = new SwaggerParser().read("/api_spa.yaml");
        assertThat(rule.validate(swagger)).isEmpty();
    }
}
