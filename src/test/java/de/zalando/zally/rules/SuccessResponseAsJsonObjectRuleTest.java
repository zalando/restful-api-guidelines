package de.zalando.zally.rules;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class SuccessResponseAsJsonObjectRuleTest {

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
