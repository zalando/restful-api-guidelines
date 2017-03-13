package de.zalando.zally.cli;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

public class Violation {
    private String title;
    private String description;
    private String violationType;
    private String ruleLink;
    private List<String> paths;

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
}
