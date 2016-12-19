package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/api-violations")
public class ApiViolationsController {

    private final ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.POST)
    public JsonNode index() {
        ObjectNode response = mapper.createObjectNode();
        response.putArray("violations");
        return response;
    }
}
