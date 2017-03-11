package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;


public class ViolationsCount {
    private JsonObject response;

    public ViolationsCount(JsonObject response) {
        this.response = response;
    }

    public Integer get(String violationType) {
        return response.getInt(violationType, 0);
    }
}
