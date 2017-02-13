package de.zalando.zally;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class Violation {

    public static final String DEFAULT_GUIDELINES_LINK = "http://zalando.github.io/restful-api-guidelines";

    private final String title;
    private final String description;
    private final ViolationType violationType;
    private final String ruleLink;
    private final Optional<String> path;

    public Violation(String title, String description, ViolationType violationType, String ruleLink) {
        this(title, description, violationType, ruleLink, Optional.empty());
    }

    public Violation(String title, String description, ViolationType violationType, String ruleLink, String path) {
        this(title, description, violationType, ruleLink, Optional.of(path));
    }

    public Violation(String title, String description, ViolationType violationType, String ruleLink, Optional<String> path) {
        this.title = title;
        this.description = description;
        this.violationType = violationType;
        this.ruleLink = ruleLink;
        this.path = path;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("violation_type")
    public ViolationType getViolationType() {
        return violationType;
    }

    @JsonProperty("rule_link")
    public String getRuleLink() {
        return ruleLink;
    }

    @JsonProperty("path")
    public Optional<String> getPath() {
        return path;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Violation)) return false;

        Violation violation = (Violation) object;

        if (!title.equals(violation.title)) return false;
        if (!description.equals(violation.description)) return false;
        if (!violationType.equals(violation.violationType)) return false;
        if (!ruleLink.equals(violation.ruleLink)) return false;
        if (!path.equals(violation.path)) return false;

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + violationType.hashCode();
        result = 31 * result + ruleLink.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }


    @Override
    public String toString() {
        return "Violation{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", violationType=" + violationType +
                ", ruleLink='" + ruleLink + '\'' +
                ", path=" + path +
                '}';
    }
}
