package de.zalando.zally.cli;

import de.zalando.zally.cli.domain.Violation;

import java.util.List;
import java.util.stream.Collectors;

public class ViolationsFilter {

    private final List<Violation> violations;

    public ViolationsFilter(List<Violation> violations) {
        this.violations = violations;
    }

    public List<Violation> getViolations(String violationType) {
        return violations
                .stream()
                .filter(v -> v.getViolationType().equalsIgnoreCase(violationType))
                .collect(Collectors.toList());
    }
}
