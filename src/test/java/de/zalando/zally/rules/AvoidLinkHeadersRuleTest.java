package de.zalando.zally.rules;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AvoidLinkHeadersRuleTest {

    @Test
    public void avoidLinkHeadersValidJson() throws IOException {
        AvoidLinkHeadersRule rule = new AvoidLinkHeadersRule();
        Swagger swagger = new SwaggerParser().read("/api_spp.json");
        assertThat(rule.validate(swagger)).isEmpty();
    }

    @Test
    public void avoidLinkHeadersValidYaml() throws IOException {
        AvoidLinkHeadersRule rule = new AvoidLinkHeadersRule();
        Swagger swagger = new SwaggerParser().read("/api_spa.yaml");
        assertThat(rule.validate(swagger)).isEmpty();
    }

    @Test
    public void avoidLinkHeadersInvalidJson() throws IOException {
        AvoidLinkHeadersRule rule = new AvoidLinkHeadersRule();
        Swagger swagger = new SwaggerParser().read("/fixtures/avoidLinkHeaderRuleInvalid.json");
        assertThat(rule.validate(swagger).size()).isEqualTo(2);
    }
}
