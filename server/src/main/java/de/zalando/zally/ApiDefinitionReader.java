package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import de.zalando.zally.exception.MissingApiDefinitionException;

public class ApiDefinitionReader {
    private final JsonNode requestBody;

    public ApiDefinitionReader(JsonNode requestBody) {
        this.requestBody = requestBody;
    }

    public String read() throws MissingApiDefinitionException {
        if (requestBody.has("api_definition")) {
            return requestBody.get("api_definition").toString();
        } else {
            throw new MissingApiDefinitionException();
        }
    }
}
