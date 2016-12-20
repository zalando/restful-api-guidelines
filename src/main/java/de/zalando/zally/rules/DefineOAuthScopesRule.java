package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.List;

public class DefineOAuthScopesRule implements Rule {
    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();
        // TODO implement validation of this rule
        return violations;
    }
}
