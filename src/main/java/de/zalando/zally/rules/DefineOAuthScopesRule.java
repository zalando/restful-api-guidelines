package de.zalando.zally.rules;

import com.google.common.collect.Sets;
import de.zalando.zally.Violation;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.auth.SecuritySchemeDefinition;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Set<String> applicableScopes = new HashSet<>(getDefinedScopes(swagger));
        applicableScopes.add(UID);
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
    private List<Violation> validateOperation(Operation operation, String path, Set<String> definedScopes) {
        List<Violation> violations = new ArrayList<>();
        Set<String> actualScopes = extractAppliedScopes(operation);
        Sets.SetView<String> illegalScopes = Sets.difference(actualScopes, definedScopes);
        violations.addAll(illegalScopes
                .stream()
                .map(s -> createViolation(path, format(INVALID_SCOPE_DESC, s, operation.getSummary())))
                .collect(Collectors.toList()));
        if (illegalScopes.size() == actualScopes.size()) {
            violations.add(createViolation(path, format(NOT_DEFINED_DESC, operation.getSummary())));
        }
        return violations;
    }

    // Extract all oauth2 scopes applied to the given operation into a simple list
    private Set<String> extractAppliedScopes(Operation operation) {
        if (operation.getSecurity() == null) {
            return Collections.emptySet();
        }
        return operation.getSecurity()
                .stream()
                .flatMap(map -> map != null ? map.get(OAUTH2).stream() : Stream.empty())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    // create a violation for an operation under a path
    private Violation createViolation(String path, String desc) {
        return new Violation(TITLE, desc, MUST, RULE_LINK, path);
    }

    // get the scopes from security definition
    private Set<String> getDefinedScopes(Swagger swagger) {
        Set<String> result = new HashSet<>();
        if (swagger.getSecurityDefinitions() != null) {
            Map<String, SecuritySchemeDefinition> securityDefinitions = swagger.getSecurityDefinitions();
            SecuritySchemeDefinition securitySchemeDefinition = securityDefinitions.get(OAUTH2);
            if (securitySchemeDefinition != null && securitySchemeDefinition instanceof OAuth2Definition) {
                OAuth2Definition oAuth2Definition = (OAuth2Definition) securitySchemeDefinition;
                result = oAuth2Definition.getScopes().keySet();
            }
        }
        return result;
    }
}
