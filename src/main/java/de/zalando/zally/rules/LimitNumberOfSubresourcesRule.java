package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LimitNumberOfSubresourcesRule implements Rule {

    private final long SUBRESOURCES_LIMIT = 3;

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();

        swagger.getPaths().forEach((path, pathObject) -> {
            String[] resourcesNames = path.split("/");
            long resourcesNamesCount = Arrays.stream(resourcesNames)
                    .filter(p -> !p.matches(""))
                    .filter(p -> !p.matches("\\{.*\\}"))
                    .count();
            long subresourcesNamesCount = resourcesNamesCount - 1;
            if (subresourcesNamesCount > SUBRESOURCES_LIMIT) {
                violations.add(getViolation(subresourcesNamesCount, path));
            }
        });
        return violations;
    }

    private Violation getViolation(long numberOfSubresources, String path) {
        return new Violation(
                "Should: Limit number of Sub-resources level",
                String.format("Number of sub-resources (%s) violates the limit of %s.", numberOfSubresources, SUBRESOURCES_LIMIT),
                ViolationType.SHOULD,
                "http://zalando.github.io/restful-api-guidelines/resources/Resources.html#should-limit-number-of-subresource-levels",
                path
        );
    }
}
