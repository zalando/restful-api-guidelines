package de.zalando.zally;

import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadlerUsing;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.exception.MissingApiDefinitionException;
import net.jadler.stubbing.server.jdk.JdkStubHttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ApiDefinitionReaderTest {

    @Before
    public void setUp() {
        initJadlerUsing(new JdkStubHttpServer());
    }

    @After
    public void tearDown() {
        closeJadler();
    }

    @Test(expected=MissingApiDefinitionException.class)
    public void shouldThrowMissingApiDefinitionExceptionWhenDefinitionIsNotFound() {
        final ApiDefinitionReader reader = new ApiDefinitionReader(getJsonNodeWithoutApiDefinition());
        reader.read();
    }

    @Test
    public void shouldReturnStringWhenApiDefinitionIsFound() {
        final ApiDefinitionReader reader = new ApiDefinitionReader(getJsonNodeWithEmptyApiDefinition());
        final String result = reader.read();
        assertEquals("{\"swagger\":\"2.0\"}", result);
    }

    @Test
    public void shouldReadJsonSwaggerDefinitionFromUrl() {
        final String fileName = "test.json";
        final String content = "{\"swagger\":\"2.0\"}";
        final String contentType = "application/json";
        final String url = startServer(fileName, content, contentType);

        final ApiDefinitionReader reader = new ApiDefinitionReader(getJsonNodeWithApiDefinitionUrl(url));
        final String result = reader.read();

        assertEquals(content, result);
    }

    @Test
    public void shouldReadYamlSwaggerDefinitionFromUrl() {
        final String fileName = "test.yaml";
        final String content = "swagger: \"2.0\"";
        final String contentType = "application/x-yaml";
        final String url = startServer(fileName, content, contentType);

        final ApiDefinitionReader reader = new ApiDefinitionReader(getJsonNodeWithApiDefinitionUrl(url));
        final String result = reader.read();

        assertEquals(content, result);
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
