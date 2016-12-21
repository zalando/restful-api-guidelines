package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.rules.RulesValidator;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GithubController {
    public static final String EXTERNAL_URL = System.getenv("external.url");
    public static final String TOKEN = System.getenv("github.token");
    private final RulesValidator rulesValidator;
    private final ObjectMapper mapper;

    @Autowired
    public GithubController(RulesValidator rulesValidator, ObjectMapper mapper) {
        this.rulesValidator = rulesValidator;
        this.mapper = mapper;
    }

    @RequestMapping(value = "/github-hook", method = RequestMethod.POST)
    public ResponseEntity<JsonNode> githubHook(@RequestBody JsonNode payload) {
        ObjectNode response = mapper.createObjectNode().put("status", "ok");
        if (payload.get("pull_request") == null) {
            return ResponseEntity.ok(response);
        }
        String repoUrl = payload.get("repository").get("url").asText();
        String commitSha = payload.get("pull_request").get("head").get("sha").asText();

        setCommitStatus(repoUrl, commitSha, "pending", "", "Waiting for Zally approval");
        try {
            List<Violation> violations = validateCommit(repoUrl, commitSha);
            if (violations.isEmpty()) {
                setCommitStatus(repoUrl, commitSha, "success", "", "Zally check passed");
            } else {
                String detailsUrl = EXTERNAL_URL + "/details/" + encode(repoUrl + " " + commitSha);
                setCommitStatus(repoUrl, commitSha, "failure", detailsUrl, "Zally found violations");
            }
        } catch (RestClientException e) {
            setCommitStatus(repoUrl, commitSha, "error", "", "Error in Zally");
        }

        return ResponseEntity.ok(response);
    }

    private List<Violation> validateCommit(String repoUrl, String commitSha) {
        String pathToFile = "swagger/swagger.yaml"; //TODO MK: unhardcode
        String swaggerUrl = String.format("%s/contents/%s?ref=%s", repoUrl, pathToFile, commitSha);

        RestTemplate client = new RestTemplate();
        JsonNode swaggerNode = client.getForEntity(swaggerUrl, JsonNode.class).getBody();
        String swaggerBase64 = swaggerNode.get("content").asText();
        String swaggerContent = Arrays.stream(swaggerBase64.split("\\n"))
                .map(this::decode)
                .collect(Collectors.joining());
        Swagger swagger = new SwaggerParser().parse(swaggerContent);
        List<Violation> violations = rulesValidator.validate(swagger);
        System.out.println("violations = " + violations);
        return violations;
    }

    private void setCommitStatus(String repoUrl, String commitSha, String status, String detailsUrl, String desc) {
        RestTemplate client = new RestTemplate();
        String statusUrl = String.format("%s/statuses/%s", repoUrl, commitSha);

        ObjectNode statusPayload = mapper.createObjectNode()
                .put("state", status)
                .put("target_url", detailsUrl)
                .put("description", desc)
                .put("context", "Swagger Linter");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);
        HttpEntity<JsonNode> entity = new HttpEntity<>(statusPayload, headers);
        ResponseEntity<JsonNode> statusResponse = client.postForEntity(statusUrl, entity, JsonNode.class);
        System.out.println("statusResponse = " + statusResponse);
    }

    @RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
    public ResponseEntity<JsonNode> details(@PathVariable String id) {
        String[] data = decode(id).split(" ");
        String repoUrl = data[0];
        String commitSha = data[1];
        List<Violation> violations = validateCommit(repoUrl, commitSha);
        ObjectNode response = mapper.createObjectNode();
        ArrayNode jsonViolations = response.putArray("violations");
        violations.forEach(jsonViolations::addPOJO);
        return ResponseEntity.ok(response);
    }

    private String encode(String data) {
        try {
            return Base64.getEncoder().encodeToString((data).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String decode(String encoded) {
        try {
            return new String(Base64.getDecoder().decode(encoded), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
