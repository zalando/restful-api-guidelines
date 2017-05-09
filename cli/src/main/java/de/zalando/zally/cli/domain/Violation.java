package de.zalando.zally.cli.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;


public class Violation {
    private String title;
    private String description;
    private ViolationType violationType;
    private String ruleLink;
    private List<String> paths = new ArrayList<>();

    public Violation(final String title, final String description) {
        this.title = title;
        this.description = description;
    }

    public Violation(JSONObject violationJson) {
        this.title = violationJson.getString("title");
        this.description = violationJson.getString("description");
        this.violationType = ViolationType.valueOf(violationJson.getString("violation_type").toUpperCase());

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

    public ViolationType getViolationType() {
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

    public void setViolationType(ViolationType violationType) {
        this.violationType = violationType;
    }

    public void setRuleLink(String ruleLink) {
        this.ruleLink = ruleLink;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
