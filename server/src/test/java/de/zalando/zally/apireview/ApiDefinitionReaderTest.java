package de.zalando.zally.apireview;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.exception.MissingApiDefinitionException;
import net.jadler.stubbing.server.jdk.JdkStubHttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadlerUsing;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;
import static org.junit.Assert.assertEquals;

public class ApiDefinitionReaderTest {

    private final String contentInJson = "{\"swagger\":\"2.0\"}";

    private ApiDefinitionReader reader;

    @Before
    public void setUp() {
        initJadlerUsing(new JdkStubHttpServer());
        reader = new ApiDefinitionReader(new RestTemplate());
    }

    @After
    public void tearDown() {
        closeJadler();
    }

    @Test(expected = MissingApiDefinitionException.class)
    public void shouldThrowMissingApiDefinitionExceptionWhenDefinitionIsNotFound() {
        reader.read(getJsonNodeWithoutApiDefinition());
    }

    @Test
    public void shouldReturnStringWhenApiDefinitionIsFound() {
        final String result = reader.read(getJsonNodeWithEmptyApiDefinition());
        assertEquals("{\"swagger\":\"2.0\"}", result);
    }

    @Test
    public void shouldReadJsonSwaggerDefinitionFromUrl() {
        final String fileName = "test.json";
        final String contentType = "application/json";
        final String url = startServer(fileName, contentInJson, contentType);

        final String result = reader.read(getJsonNodeWithApiDefinitionUrl(url));

        assertEquals(contentInJson, result);
    }

    @Test
    public void shouldReadYamlSwaggerDefinitionFromUrl() {
        final String fileName = "test.yaml";
        final String content = "swagger: \"2.0\"";
        final String contentType = "application/x-yaml";
        final String url = startServer(fileName, content, contentType);

        final String result = reader.read(getJsonNodeWithApiDefinitionUrl(url));

        assertEquals(content, result);
    }

    @Test
    public void shouldRetryLoadingOfUrlIfEndsWithSpecialEncodedCharacters() {
        final String result = reader.read(getJsonNodeWithApiDefinitionUrlWithSpecialCharacters());
        assertEquals(contentInJson, result);
    }

    private JsonNode getJsonNodeWithoutApiDefinition() {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode node = mapper.createObjectNode();
        node.putObject("some_definition");
        return node;
    }

    private JsonNode getJsonNodeWithEmptyApiDefinition() {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode node = mapper.createObjectNode();
        final ObjectNode apiDefinitionNode = node.putObject("api_definition");
        apiDefinitionNode.put("swagger", "2.0");
        return node;
    }

    private JsonNode getJsonNodeWithApiDefinitionUrlWithSpecialCharacters() {
        final String fileName = "test.json";
        final String contentType = "application/json";
        return getJsonNodeWithApiDefinitionUrl(startServer(fileName, contentInJson, contentType) + "%3D%3D");
    }

    private JsonNode getJsonNodeWithApiDefinitionUrl(String url) {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode node = mapper.createObjectNode();
        node.put("api_definition_url", url);
        return node;
    }

    private String startServer(final String fileName, final String content, final String contentType) {
        final String remotePath = "/" + fileName;
        final String url = "http://localhost:" + port() + remotePath;

        onRequest()
            .havingMethodEqualTo("GET")
            .havingPathEqualTo(remotePath)
            .respond()
            .withStatus(200)
            .withHeader("Content-Type", contentType)
            .withBody(content);

        return url;
    }
}
