package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.zalando.zally.rules.RulesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class DetailsController {
    private final ObjectMapper mapper;
    private GithubClient githubClient;
    private final RulesValidator rulesValidator;

    @Autowired
    public DetailsController(ObjectMapper mapper, GithubClient githubClient, RulesValidator rulesValidator) {
        this.mapper = mapper;
        this.githubClient = githubClient;
        this.rulesValidator = rulesValidator;
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public String details(@RequestParam(value = "id") String id, Map<String, Object> model) {
        try {
            System.out.println("GithubClient.decode(id) = " + GithubClient.decode(id));
            JsonNode json = mapper.readTree(GithubClient.decode(id));
            String repoUrl = json.get("repo_url").asText();
            String commitSha = json.get("commit_sha").asText();
            String swaggerPath = json.get("swagger_path").asText();
            String swaggerContent = githubClient.getFileContent(repoUrl, commitSha, swaggerPath);
            List<Violation> violations = rulesValidator.validate(swaggerContent);

            model.put("violations", violations);
            return "details";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String submit(Map<String, Object> model) {
        return "try";
    }
}
