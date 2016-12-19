package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.auth.SecuritySchemeDefinition;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class SecureWithOAuth2RuleTest {
    private final SecureWithOAuth2Rule rule = new SecureWithOAuth2Rule();

    private void assertViolationOccurs(Swagger swagger) {
        List<Violation> violations = rule.validate(swagger);
        assertEquals(1, violations.size());

        Violation violation = violations.get(0);
        assertEquals("Secure Endpoints with OAuth 2.0", violation.getTitle());
        assertEquals("No OAuth2 security definitions found", violation.getDescription());
        assertEquals(ViolationType.MUST, violation.getViolationType());
        assertEquals(rule.LINK, violation.getRuleLink());
    }

    private void assertNoViolations(Swagger swagger) {
        List<Violation> violations = rule.validate(swagger);
        assertEquals(0, violations.size());
    }

    @Test
    public void returnsViolationsWhenSecurityDefinitionsAreNotInitialized() {
        Swagger swagger = new Swagger();
        assertViolationOccurs(swagger);
    }

    @Test
    public void returnsViolationsWhenNoSecurityDefinitionsAreSpecified() {
        Swagger swagger = new Swagger();
        swagger.setSecurityDefinitions(new HashMap<>());
        assertViolationOccurs(swagger);
    }

    @Test
    public void returnsViolationsWhenNoOAuth2SecurityDefinitionsAreSpecified() {
        Map<String, SecuritySchemeDefinition> definitions = new HashMap<>();
        definitions.put("Basic", new BasicAuthDefinition());
        definitions.put("ApiKey", new ApiKeyAuthDefinition());

        Swagger swagger = new Swagger();
        swagger.setSecurityDefinitions(definitions);

        assertViolationOccurs(swagger);
    }

    @Test
    public void returnsNoViolationsWhenOAuth2SecurityDefinitionsIsSpecified() {
        Map<String, SecuritySchemeDefinition> definitions = new HashMap<>();
        definitions.put("Basic", new BasicAuthDefinition());
        definitions.put("Oauth2", new OAuth2Definition());

        Swagger swagger = new Swagger();
        swagger.setSecurityDefinitions(definitions);

        assertNoViolations(swagger);
    }
}