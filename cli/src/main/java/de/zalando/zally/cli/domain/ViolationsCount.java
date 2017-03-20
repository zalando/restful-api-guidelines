package de.zalando.zally.cli.domain;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;


public class ViolationsCount {
    private Map<String, Integer> counters;

    public ViolationsCount(Map<String, Integer> counters) {
        this.counters = counters;
    }

    public ViolationsCount(JSONObject response) {
        counters = new HashMap<>();
        for (Map.Entry<String, Object> entry : response.toMap().entrySet()) {
            if (entry.getValue() instanceof Integer) {
                counters.put(entry.getKey(), (Integer) entry.getValue());
            }
        }
    }

    public Integer get(String violationType) {
        return counters.getOrDefault(violationType, 0);
    }
}
