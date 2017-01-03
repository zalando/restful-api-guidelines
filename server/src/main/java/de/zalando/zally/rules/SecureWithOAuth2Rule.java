package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;
import io.swagger.models.auth.SecuritySchemeDefinition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SecureWithOAuth2Rule implements Rule {
    public final String LINK =
            "https://zalando.github.io/restful-api-guidelines/security/Security.html" +
                    "#must-secure-endpoints-with-oauth-20";
    public final String TITLE = "Secure Endpoints with OAuth 2.0";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();
        Map<String, SecuritySchemeDefinition> securityDefinitions = swagger.getSecurityDefinitions();

        if (!hasAnyOauth2SecurityDefinition(securityDefinitions)) {
            violations.add(
                    new Violation(TITLE, "No OAuth2 security definitions found", ViolationType.MUST, LINK)
            );
        }

        return violations;
    }

    private boolean hasAnyOauth2SecurityDefinition(Map<String, SecuritySchemeDefinition> securityDefinitions) {
        if (securityDefinitions == null) {
            return false;
        }

        return securityDefinitions
                .values()
                .stream()
                .anyMatch(v -> v.getType().toLowerCase().equals("oauth2"));
    }
}
