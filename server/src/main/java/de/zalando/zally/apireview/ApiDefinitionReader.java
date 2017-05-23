package de.zalando.zally.apireview;

import com.fasterxml.jackson.databind.JsonNode;
import de.zalando.zally.exception.MissingApiDefinitionException;
import de.zalando.zally.exception.UnaccessibleResourceUrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiDefinitionReader {

    // some internal systems add these characters at the end of some urls, don't know why
    private static final String SPECIAL_CHARACTERS_SUFFIX = "%3D%3D";

    private final RestTemplate client;

    @Autowired
    public ApiDefinitionReader(RestTemplate client) {
        this.client = client;
    }

    public String read(JsonNode requestBody) throws MissingApiDefinitionException, UnaccessibleResourceUrlException {
        if (requestBody.has("api_definition")) {
            return requestBody.get("api_definition").toString();
        } else if (requestBody.has("api_definition_url")) {
            return readFromUrl(requestBody.get("api_definition_url").textValue());
        } else {
            throw new MissingApiDefinitionException();
        }
    }

    private String readFromUrl(String url) throws UnaccessibleResourceUrlException {
        try {
            return client.getForEntity(removeSpecialCharactersSuffix(url), String.class).getBody();
        } catch (HttpClientErrorException exception) {
            throw new UnaccessibleResourceUrlException(exception.getMessage(), exception.getStatusCode());
        } catch (ResourceAccessException exception) {
            throw new UnaccessibleResourceUrlException("Unknown host: " + exception.getCause().getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private String removeSpecialCharactersSuffix(String url) {
        return url.endsWith(SPECIAL_CHARACTERS_SUFFIX)
            ? url.substring(0, url.length() - SPECIAL_CHARACTERS_SUFFIX.length())
            : url;
    }
}
