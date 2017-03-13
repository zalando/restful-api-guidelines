package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ViolationsCountTest {
    @Test
    public void returnsViolationsByType() {
        final JsonObject jsonCounters = new JsonObject();
        jsonCounters.add("must", 1);

        final ViolationsCount violationsCount = new ViolationsCount(jsonCounters);
        assertEquals(1, violationsCount.get("must").intValue());
    }

    @Test
    public void returnsZeroWhenViolationTypeNotFound() {
        final JsonObject jsonCounters = new JsonObject();
        jsonCounters.add("must", 1);

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