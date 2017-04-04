package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import de.zalando.zally.exception.MissingApiDefinitionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ApiDefinitionReader {
    private final JsonNode requestBody;

    public ApiDefinitionReader(JsonNode requestBody) {
        this.requestBody = requestBody;
    }

    public String read() throws MissingApiDefinitionException {
        if (requestBody.has("api_definition")) {
            return requestBody.get("api_definition").toString();
        } else if (requestBody.has("api_definition_url")) {
            return readFromUrl(requestBody.get("api_definition_url").textValue());
        } else {
            throw new MissingApiDefinitionException();
        }
    }

    protected String readFromUrl(String url) {
        final RestTemplate client = new RestTemplate();
        final ResponseEntity<String> response = client.getForEntity(url, String.class);
        return response.getBody();
    }
}
