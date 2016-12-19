package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.Swagger;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This validator validates a given Swagger definition based
 * on set of rules. It will sort the output by path.
 */
public class RulesValidator implements Rule {

    private final List<Rule> rules;

    public RulesValidator(List<Rule> rules) {
        this.rules = new LinkedList<>(rules);
    }

    @Override
    public List<Violation> validate(Swagger swagger) {
        return rules.stream()
                .flatMap((rule -> rule.validate(swagger).stream()))
                .sorted((violation, other) -> {
                    if (!violation.getPath().isPresent() || !other.getPath().isPresent()) {
                        return 1;
                    }
                    return violation.getPath().get().compareToIgnoreCase(other.getPath().get());
                })
                .collect(Collectors.toList());
    }
}
