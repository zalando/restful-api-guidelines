package de.zalando.zally.cli;

import de.zalando.zally.cli.domain.Violation;
import de.zalando.zally.cli.domain.ViolationType;

import java.util.List;
import java.util.stream.Collectors;

public class ViolationsFilter {

    private final List<Violation> violations;

    public ViolationsFilter(List<Violation> violations) {
        this.violations = violations;
    }

    public List<Violation> getViolations(ViolationType violationType) {
        return violations
                .stream()
                .filter(v -> v.getViolationType().equals(violationType))
                .collect(Collectors.toList());
    }
}
