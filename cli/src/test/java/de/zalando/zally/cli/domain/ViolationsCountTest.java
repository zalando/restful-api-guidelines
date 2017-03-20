package de.zalando.zally.cli.domain;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.junit.Test;


public class ViolationsCountTest {
    @Test
    public void returnsViolationsByType() {
        final JSONObject jsonCounters = new JSONObject();
        jsonCounters.put("must", 1);

        final ViolationsCount violationsCount = new ViolationsCount(jsonCounters);
        assertEquals(1, violationsCount.get("must").intValue());
    }

    @Test
    public void returnsZeroWhenViolationTypeNotFound() {
        final JSONObject jsonCounters = new JSONObject();
        jsonCounters.put("must", 1);

        final ViolationsCount violationsCount = new ViolationsCount(jsonCounters);
        assertEquals(0, violationsCount.get("should").intValue());
    }

    @Test
    public void createsViolationUsingAlternativeConstructor() {
        final Map<String, Integer> counters = new HashMap<>();
        counters.put("must", 1);

        final ViolationsCount violationsCount = new ViolationsCount(counters);
        assertEquals(1, violationsCount.get("must").intValue());
        assertEquals(0, violationsCount.get("should").intValue());
    }
}