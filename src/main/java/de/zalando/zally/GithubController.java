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
import org.springframework.web.client.RestClientException;

import java.util.List;

@RestController
public class GithubController {
    private  static final String EXTERNAL_URL = System.getenv("external.url"); //TODO MK: introduce proper config

    private final GithubClient githubClient;
    private final RulesValidator rulesValidator;
    private final ObjectMapper mapper;

    @Autowired
    public GithubController(RulesValidator rulesValidator, ObjectMapper mapper, GithubClient githubClient) {
        this.rulesValidator = rulesValidator;
        this.mapper = mapper;
        this.githubClient = githubClient;
    }

    @RequestMapping(value = "/github-hook", method = RequestMethod.POST)
    public ResponseEntity<JsonNode> githubHook(@RequestBody JsonNode payload) {
        ObjectNode response = mapper.createObjectNode().put("status", "ok");
        if (payload.get("pull_request") != null) {
            String repoUrl = payload.get("repository").get("url").asText();
            String commitSha = payload.get("pull_request").get("head").get("sha").asText();

            githubClient.setCommitStatus(repoUrl, commitSha, "pending", "", "Waiting for Zally approval");
            try {
                String swaggerPath = "swagger/swagger.yaml"; //TODO MK: unhardcode it
                String swaggerContent = githubClient.getFileContent(repoUrl, commitSha, swaggerPath);
                Swagger swagger = new SwaggerParser().parse(swaggerContent);
                List<Violation> violations = rulesValidator.validate(swagger);
                if (violations.isEmpty()) {
                    githubClient.setCommitStatus(repoUrl, commitSha, "success", "", "Zally check passed");
                } else {
                    String id = mapper.createObjectNode()
                            .put("swagger_path", swaggerPath)
                            .put("repo_url", repoUrl)
                            .put("commit_sha", commitSha).toString();
                    String detailsUrl = EXTERNAL_URL + "/details/" + GithubClient.encode(id);
                    githubClient.setCommitStatus(repoUrl, commitSha, "failure", detailsUrl, "Zally found violations");
                }
            } catch (RestClientException e) {
                githubClient.setCommitStatus(repoUrl, commitSha, "error", "", "Error in Zally");
            }

        }
        return ResponseEntity.ok(response);
    }
}
