package de.zalando.zally.cli.api;

import de.zalando.zally.cli.readers.SpecsReader;
import org.json.JSONObject;


public class RequestDecorator {
    private SpecsReader reader;

    public RequestDecorator(SpecsReader reader) {
        this.reader = reader;
    }

    public String getRequestBody() {
        final JSONObject specification = reader.read();
        final JSONObject wrapper = new JSONObject().put("api_definition", specification);
        return wrapper.toString();
    }
}
