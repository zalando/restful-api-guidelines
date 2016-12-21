package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.rules.RulesValidator;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GithubController {

    private final RulesValidator rulesValidator;
    private final ObjectMapper mapper;

    @Autowired
    public GithubController(RulesValidator rulesValidator, ObjectMapper mapper) {
        this.rulesValidator = rulesValidator;
        this.mapper = mapper;
    }

    @RequestMapping(value = "/github-hook", method = RequestMethod.POST)
    public ResponseEntity<JsonNode> githubHook(@RequestBody JsonNode payload) {
        String repoUrl = payload.get("repository").get("url").asText();
        String commitSha = payload.get("pull_request").get("head").get("sha").asText();
        String pathToFile = "swagger/swagger.yaml";
        String swaggerUrl = String.format("%s/contents/%s?ref=%s", repoUrl, pathToFile, commitSha);

        RestTemplate client = new RestTemplate();
        JsonNode swaggerNode = client.getForEntity(swaggerUrl, JsonNode.class).getBody();
        String swaggerBase64 = swaggerNode.get("content").asText();

        String swaggerContent = Arrays.stream(swaggerBase64.split("\\n"))
                .map(this::decodeLine)
                .collect(Collectors.joining());
        Swagger swagger = new SwaggerParser().parse(swaggerContent);

        List<Violation> violations = rulesValidator.validate(swagger);

        String statusUrl = String.format("%s/statuses/%s", repoUrl, commitSha);
        System.out.println("statusUrl = " + statusUrl);
        ObjectNode status = mapper.createObjectNode()
                .put("state", "pending")
                .put("target_url", "http://example.org")
                .put("description", "Pending!!!")
                .put("context", "Swagger Linter");
        ResponseEntity<JsonNode> statusResponse = client.postForEntity(statusUrl, status, JsonNode.class);
        System.out.println("statusResponse = " + statusResponse);
        ObjectNode response = mapper.createObjectNode().put("status", swaggerContent);
        return ResponseEntity.ok(response);
    }

    private String decodeLine(String encoded) {
        try {
            return new String(Base64.getDecoder().decode(encoded), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
