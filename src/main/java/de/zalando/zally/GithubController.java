package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.stream.Collectors;

@RestController
public class GithubController {

    private final ObjectMapper mapper;

    @Autowired
    public GithubController(ObjectMapper mapper) {
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
                .collect(Collectors.joining("\n"));

        System.out.println(swaggerContent);

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
