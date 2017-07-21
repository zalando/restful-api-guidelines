package de.zalando.zally.dto;

import de.zalando.zally.rule.Violation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViolationsCounter {
    private final Map<ViolationType, Integer> counters;

    public ViolationsCounter(List<Violation> violations) {
        counters = violations.
            stream().
            collect(Collectors.groupingBy(Violation::getViolationType, Collectors.summingInt(x -> 1)));
    }

    public Integer getCounter(ViolationType violationType) {
        return counters.getOrDefault(violationType, 0);
    }
}
