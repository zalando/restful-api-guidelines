package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.auth.SecuritySchemeDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.zalando.zally.ViolationType.MUST;
import static java.lang.String.format;

public class DefineOAuthScopesRule implements Rule {
    private static final String TITLE = "Define and Assign Access Rights (Scopes)";
    private static final String NOT_DEFINED_DESC = "No valid OAuth2 scope is defined for endpoint %s";
    private static final String INVALID_SCOPE_DESC = "Invalid OAuth2 scope %s for the endpoint %s";
    private static final String RULE_LINK = "http://zalando.github.io/restful-api-guidelines/security/Security.html";
    static final String OAUTH2 = "oauth2";
    static final String UID = "uid";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();

        if (swagger == null || swagger.getPaths() == null) {
            return violations;
        }

        Map<String, String> applicableScopes = getDefinedScopes(swagger);
        applicableScopes.put(UID, "applicable by default");

        swagger.getPaths().forEach((pathKey, path) -> {
            if (path != null) {
                path.getOperations().forEach(operation -> {
                    violations.addAll(validateOperation(operation, pathKey, applicableScopes));
                });
            }
        });
        return violations;
    }

    // Validate one endpoint
    private List<Violation> validateOperation(Operation operation, String path,
                                              Map<String, String> applicableScopes) {
        List<Violation> violations = new ArrayList<>();

        boolean validScopePresent = false;
        for (String appliedScope : extractAppliedScopes(operation)) {
            if (applicableScopes.containsKey(appliedScope)) {
                validScopePresent = true;
            } else {
                String description = format(INVALID_SCOPE_DESC, appliedScope, operation.getSummary());
                violations.add(createViolation(path, description));
            }
        }

        if (!validScopePresent) {
            String description = format(NOT_DEFINED_DESC, operation.getSummary());
            violations.add(createViolation(path, description));
        }
        return violations;
    }

    // Extract all oauth2 scopes applied to the given operation into a simple list
    private List<String> extractAppliedScopes(Operation operation) {
        List<String> scopes = new ArrayList<>();
        List<Map<String, List<String>>> security = operation.getSecurity();
        if (security != null || !security.isEmpty()) {
            security.forEach(securityMap -> {
                if (securityMap != null) {
                    securityMap.forEach((key, value) -> {
                        if (OAUTH2.equals(key) && value != null) {
                            scopes.addAll(value);
                        }
                    });
                }
            });
        }
        return scopes;
    }

    // create a violation for an operation under a path
    private Violation createViolation(String path, String desc) {
        return new Violation(TITLE, desc, MUST, RULE_LINK, path);
    }

    // get the scopes from security definition
    private Map<String, String> getDefinedScopes(Swagger swagger) {
        Map<String, String> scopes = null;
        if (swagger.getSecurityDefinitions() != null) {
            Map<String, SecuritySchemeDefinition> securityDefinitions = swagger.getSecurityDefinitions();
            SecuritySchemeDefinition securitySchemeDefinition = securityDefinitions.get(OAUTH2);
            if (securitySchemeDefinition != null && securitySchemeDefinition instanceof OAuth2Definition) {
                OAuth2Definition oAuth2Definition = (OAuth2Definition) securitySchemeDefinition;
                scopes = oAuth2Definition.getScopes();
            }
        }
        if (scopes == null) {
            scopes = new HashMap<>();
        }
        return scopes;
    }
}
