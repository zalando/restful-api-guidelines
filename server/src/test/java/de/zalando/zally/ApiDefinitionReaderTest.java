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
    public void shouldReadSwaggerDefinitionFromUrl() {
        final String remotePath = "/test.json";
        final String url = "http://localhost:" + port() + remotePath;
        final String responseBody = "{\"swagger\":\"2.0\"}";

        onRequest()
                .havingMethodEqualTo("GET")
                .havingPathEqualTo(remotePath)
                .respond()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody);

        final ApiDefinitionReader reader = new ApiDefinitionReader(getJsonNodeWithApiDefinitionUrl(url));
        final String result = reader.read();

        assertEquals(responseBody, result);
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
}