package de.zalando.zally;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.exception.MissingApiDefinitionException;
import org.junit.Test;

public class ApiDefinitionReaderTest {

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
}