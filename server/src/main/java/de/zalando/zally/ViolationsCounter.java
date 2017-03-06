package de.zalando.zally;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViolationsCounter {
    private final Map<ViolationType, Integer> counters = new HashMap<>();

    public ViolationsCounter(List<Violation> violations) {
        countViolations(violations);
    }

    public Integer getCounter(ViolationType violationType) {
        return counters.getOrDefault(violationType, 0);
    }

    private void countViolations(List<Violation> violations) {
        violations.forEach(violation -> updateCounter(violation));
    }

    private void updateCounter(Violation violation) {
        ViolationType violationType = violation.getViolationType();
        Integer count = counters.getOrDefault(violationType, 0) + 1;
        counters.put(violationType, count);
    }
}
