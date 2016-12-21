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

public class Use429ForRateLimitRuleTest {

    @Test
    public void responseUse429ForRateLimitRuleNoViolationsJson() throws IOException {
        Use429ForRateLimitRule rule = new Use429ForRateLimitRule();
        Swagger swagger = new SwaggerParser().read("/api_spp.json");
        List<Violation> violations = rule.validate(swagger);
        assertThat(violations.size()).isEqualTo(9);
        assertTrue(violations.stream().allMatch(v -> ViolationType.MUST.equals(v.getViolationType())));
        assertTrue(violations.stream().allMatch(v -> "Use 429 With Header For Rate Limits".equals(v.getTitle())));
    }

    @Test
    public void responseUse429ForRateLimitRuleNoViolationYaml() throws IOException {
        Use429ForRateLimitRule rule = new Use429ForRateLimitRule();
        Swagger swagger = new SwaggerParser().read("/api_spa.yaml");
        assertThat(rule.validate(swagger)).isEmpty();
    }
}
