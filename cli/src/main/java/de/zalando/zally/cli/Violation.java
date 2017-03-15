package de.zalando.zally.cli;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Violation {
    private String title;
    private String description;
    private String violationType;
    private String ruleLink;
    private List<String> paths = new ArrayList<>();

    public Violation(final String title, final String description) {
        this.title = title;
        this.description = description;
    }

    public Violation(JsonObject violationJson) {
        this.title = violationJson.get("title").asString();
        this.description = violationJson.get("description").asString();

        if (violationJson.get("violation_type") != null && violationJson.get("violation_type").isString()) {
            this.violationType = violationJson.get("violation_type").asString();
        }

        if (violationJson.get("rule_link") != null && violationJson.get("rule_link").isString()) {
            this.ruleLink = violationJson.get("rule_link").asString();
        }

        if (violationJson.get("paths") != null && violationJson.get("paths").isArray()) {
            JsonArray paths = violationJson.get("paths").asArray();
            this.paths = paths.values().stream().map(x -> x.asString()).collect(Collectors.toList());
        }
    }

    public Violation(JSONObject violationJson) {
        this.title = violationJson.getString("title");
        this.description = violationJson.getString("description");

        if (violationJson.has("violation_type")) {
            this.violationType = violationJson.getString("violation_type");
        }

        if (violationJson.has("rule_link")) {
            this.ruleLink = violationJson.getString("rule_link");
        }

        if (violationJson.has("paths") && violationJson.get("paths") instanceof JSONArray) {
            JSONArray paths = violationJson.getJSONArray("paths");
            this.paths = paths.toList().stream().map(x -> (String) x).collect(Collectors.toList());
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getViolationType() {
        return violationType;
    }

    public String getRuleLink() {
        return ruleLink;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setViolationType(String violationType) {
        this.violationType = violationType;
    }

    public void setRuleLink(String ruleLink) {
        this.ruleLink = ruleLink;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
