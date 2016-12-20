package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseAsJsonObjectRuleTest {

    @Test
    public void responseAsJsonObjectRuleNoViolationsJson() throws IOException {
        SuccessResponseAsJsonObjectRule rule = new SuccessResponseAsJsonObjectRule();
        Swagger swagger = new SwaggerParser().read("/api_spp.json");
        assertThat(rule.validate(swagger)).isEmpty();
    }

    @Test
    public void responseAsJsonObjectRuleNoViolationYaml() throws IOException {
        SuccessResponseAsJsonObjectRule rule = new SuccessResponseAsJsonObjectRule();
        Swagger swagger = new SwaggerParser().read("/api_spa.yaml");
        assertThat(rule.validate(swagger).size()).isEqualTo(2);
    }
}
