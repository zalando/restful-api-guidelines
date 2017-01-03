package de.zalando.zally.rules;

import com.google.common.collect.Sets;
import de.zalando.zally.Violation;
import io.swagger.models.Operation;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.auth.SecuritySchemeDefinition;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static de.zalando.zally.ViolationType.MUST;
import static java.lang.String.format;

@Component
public class DefineOAuthScopesRule implements Rule {
    private static final String TITLE = "Define and Assign Access Rights (Scopes)";
    private static final String NOT_DEFINED_DESC = "No valid OAuth2 scope is defined for endpoint %s";
    private static final String INVALID_SCOPE_DESC = "Invalid OAuth2 scope %s for the endpoint %s";
    private static final String RULE_LINK = "http://zalando.github.io/restful-api-guidelines/security/Security.html";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();
        if (swagger == null || swagger.getPaths() == null) {
            return violations;
        }


        Set<String> applicableScopes = new HashSet<>(getDefinedScopes(swagger));
        boolean isTopLevelScopeDefined = isTopLevelScopeDefined(swagger, applicableScopes);
        swagger.getPaths().forEach((pathKey, path) -> {
            if (path != null) {
                path.getOperations().forEach(operation -> {
                    violations.addAll(validateOperation(operation, pathKey, applicableScopes, isTopLevelScopeDefined));
                });
            }
        });
        return violations;
    }

    // Validate one endpoint
    private List<Violation> validateOperation(Operation operation, String path, Set<String> definedScopes, boolean isToplevelScopeDefined) {
        List<Violation> violations = new ArrayList<>();
        Set<String> actualScopes = extractAppliedScopes(operation);
        Sets.SetView<String> illegalScopes = Sets.difference(actualScopes, definedScopes);
        violations.addAll(illegalScopes
                .stream()
                .map(s -> createViolation(path, format(INVALID_SCOPE_DESC, s, operation.getSummary())))
                .collect(Collectors.toList()));
        if (illegalScopes.size() == actualScopes.size() && !isToplevelScopeDefined) {
            violations.add(createViolation(path, format(NOT_DEFINED_DESC, operation.getSummary())));
        }
        return violations;
    }

    // Extract all oauth2 scopes applied to the given operation into a simple list
    private Set<String> extractAppliedScopes(Operation operation) {
        if (operation.getSecurity() == null) {
            return Collections.emptySet();
        }
        Set<String> appliedScopes = new HashSet<>();
        operation.getSecurity().forEach(securityGroupDefinition -> {
            if (securityGroupDefinition != null) {
                securityGroupDefinition.forEach((group, scopes) -> {
                    if (group != null && scopes != null) {
                        scopes.forEach(scope -> {
                            appliedScopes.add(group + ":" + scope);
                        });
                    }
                });
            }
        });
        return appliedScopes;
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
            securityDefinitions.forEach((group, securitySchemeDefinition) -> {
                if (securitySchemeDefinition != null && securitySchemeDefinition instanceof OAuth2Definition) {
                    OAuth2Definition oAuth2Definition = (OAuth2Definition) securitySchemeDefinition;
                    oAuth2Definition.getScopes().keySet().forEach(scope -> {
                        result.add(group + ":" + scope);
                    });
                }
            });
        }
        return result;
    }

    private boolean isTopLevelScopeDefined(Swagger swagger, Set<String> applicableScopes) {
        if (swagger.getSecurity() != null) {
            for (SecurityRequirement securityGroupDefinition : swagger.getSecurity()) {
                if (securityGroupDefinition != null) {
                    for (Map.Entry<String, List<String>> entry : securityGroupDefinition.getRequirements().entrySet()) {
                        String group = entry.getKey();
                        List<String> scopes = entry.getValue();
                        if (group != null && scopes != null) {
                            for (String scope : scopes) {
                                if (applicableScopes.contains(group + ":" + scope)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
