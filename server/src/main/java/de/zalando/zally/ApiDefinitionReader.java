package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import de.zalando.zally.exception.UnaccessibleResourceUrlException;
import de.zalando.zally.exception.MissingApiDefinitionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public class ApiDefinitionReader {
    private final JsonNode requestBody;

    public ApiDefinitionReader(JsonNode requestBody) {
        this.requestBody = requestBody;
    }

    public String read() throws MissingApiDefinitionException, UnaccessibleResourceUrlException {
        if (requestBody.has("api_definition")) {
            return requestBody.get("api_definition").toString();
        } else if (requestBody.has("api_definition_url")) {
            return readFromUrl(requestBody.get("api_definition_url").textValue());
        } else {
            throw new MissingApiDefinitionException();
        }
    }

    protected String readFromUrl(String url) throws UnaccessibleResourceUrlException {
        final RestTemplate client = new RestTemplate();
        final ResponseEntity<String> response;

        try {
            response = client.getForEntity(url, String.class);
        } catch (HttpClientErrorException exception) {
            throw new UnaccessibleResourceUrlException(exception.getMessage(), exception.getStatusCode());
        } catch (ResourceAccessException exception) {
            final String message =  "Unknown host: " + exception.getCause().getMessage();
            throw new UnaccessibleResourceUrlException(message, HttpStatus.NOT_FOUND);
        }

        return response.getBody();
    }
}
