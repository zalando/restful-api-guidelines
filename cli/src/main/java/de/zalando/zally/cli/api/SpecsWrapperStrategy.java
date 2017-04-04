package de.zalando.zally.cli.api;

import de.zalando.zally.cli.reader.SpecsReader;
import org.json.JSONObject;

public class SpecsWrapperStrategy implements RequestWrapperStrategy {

    private final SpecsReader reader;

    public SpecsWrapperStrategy(SpecsReader reader) {
        this.reader = reader;
    }

    public String wrap() {
        final JSONObject specification = reader.read();
        final JSONObject wrapper = new JSONObject().put("api_definition", specification);
        return wrapper.toString();
    }
}
