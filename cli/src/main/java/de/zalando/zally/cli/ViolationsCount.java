package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;
import org.json.JSONObject;

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
