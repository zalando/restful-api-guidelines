package de.zalando.zally.cli;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

public class Violation {
    private final String title;
    private final String description;
    private final String violationType;
    private final String ruleLink;
    private final List<String> paths;

    public Violation(JsonObject violationJson) {
        JsonArray paths = violationJson.get("paths").asArray();

        this.title = violationJson.get("title").asString();
        this.description = violationJson.get("description").asString();
        this.violationType = violationJson.get("violation_type").asString();
        this.ruleLink = violationJson.get("rule_link").asString();
        this.paths = paths.values().stream().map(x -> x.asString()).collect(Collectors.toList());
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
