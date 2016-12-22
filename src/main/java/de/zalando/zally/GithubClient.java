package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

@Component
public class GithubClient {
    private static final String TOKEN = System.getenv("github.token"); //TODO MK: introduce proper config

    private final ObjectMapper mapper;

    @Autowired
    public GithubClient(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JsonNode setCommitStatus(String repoUrl, String commitSha, String status, String detailsUrl, String desc) {
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
        ResponseEntity<JsonNode> response = client.postForEntity(statusUrl, entity, JsonNode.class);
        return response.getBody();
    }

    public String getFileContent(String repoUrl, String commitSha, String pathToFile) {
        String url = String.format("%s/contents/%s?ref=%s", repoUrl, pathToFile, commitSha);
        RestTemplate client = new RestTemplate();
        JsonNode body = client.getForEntity(url, JsonNode.class).getBody();
        return Arrays.stream(body.get("content").asText().split("\\n"))
                .map(GithubClient::decode)
                .collect(Collectors.joining());
    }

    public static String encode(String data) {
        try {
            return Base64.getEncoder().encodeToString((data).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decode(String encoded) {
        try {
            return new String(Base64.getDecoder().decode(encoded), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
