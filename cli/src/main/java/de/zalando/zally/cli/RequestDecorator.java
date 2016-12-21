package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;


public class RequestDecorator {
    private SpecsReader reader;

    public RequestDecorator(SpecsReader reader) {
        this.reader = reader;
    }

    public String getRequestBody() {
        JsonObject wrapper = new JsonObject();
        JsonValue specification = reader.read();

        wrapper.add("api_definition", specification);
        return wrapper.toString();
    }
}
