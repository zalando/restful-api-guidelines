package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

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
}