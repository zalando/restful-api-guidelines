package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Rule for limiting number of resources
 */
@Component
public class LimitNumberOfResourcesRule implements Rule {

    private static final int RESOURCE_LIMIT = 8;

    @Override
    public List<Violation> validate(Swagger swagger) {
        if (swagger == null) return new ArrayList<>();

        List<Violation> violations = new ArrayList<>();
        int numberOfPaths = swagger.getPaths().size();
        if (numberOfPaths > RESOURCE_LIMIT) {
            violations.add(getViolation(numberOfPaths));
        }
        return violations;
    }

    private Violation getViolation(int numberOfPaths) {
        return new Violation(
                "Limit number of Resources",
                String.format("Number of paths (%s) violates the limit of %s.", numberOfPaths, RESOURCE_LIMIT),
                ViolationType.SHOULD,
                "http://zalando.github.io/restful-api-guidelines/resources/Resources.html#should-limit-number-of-resources"
        );
    }
}
