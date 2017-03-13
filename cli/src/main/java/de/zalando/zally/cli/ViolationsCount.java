package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;


public class ViolationsCount {
    private Map<String, Integer> counters;

    public ViolationsCount(Map<String, Integer> counters) {
        this.counters = counters;
    }

    public ViolationsCount(JsonObject response) {
        counters = new HashMap<>();
        for (String violationType: response.names()) {
            counters.put(violationType, response.getInt(violationType, 0));
        }
    }

    public Integer get(String violationType) {
        return counters.getOrDefault(violationType, 0);
    }
}
