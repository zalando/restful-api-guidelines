package de.zalando.zally;

import io.swagger.models.Path;

import java.util.Optional;

public class Violation {

    public static final String DEFAULT_GUIDELINES_LINK = "http://zalando.github.io/restful-api-guidelines";

    private final String title;
    private final String description;
    private final ViolationType violationType;
    private final String ruleLink;
    private final Optional<String> path;
    private final Optional<Integer> lineNumber;
    private final Optional<Integer> columnNumber;

    public Violation(String title, String description,
                     ViolationType violationType, String ruleLink) {
        this.title = title;
        this.description = description;
        this.violationType = violationType;
        this.ruleLink = ruleLink;
        this.path = Optional.empty();
        this.lineNumber = Optional.empty();
        this.columnNumber = Optional.empty();
    }

    public Violation(String title, String description,
                     ViolationType violationType, String ruleLink, String path) {
        this.title = title;
        this.description = description;
        this.violationType = violationType;
        this.ruleLink = ruleLink;
        this.path = Optional.of(path);
        this.lineNumber = Optional.empty();
        this.columnNumber = Optional.empty();
    }

    public Violation(String title, String description,
                     ViolationType violationType, String ruleLink,
                     int lineNumber, int columnNumber) {
        this.title = title;
        this.description = description;
        this.violationType = violationType;
        this.ruleLink = ruleLink;
        this.path = Optional.empty();
        this.lineNumber = Optional.of(lineNumber);
        this.columnNumber = Optional.of(columnNumber);
    }

    public Violation(String title, String description,
                     ViolationType violationType, String ruleLink,
                     String path, int lineNumber, int columnNumber) {
        this.title = title;
        this.description = description;
        this.violationType = violationType;
        this.ruleLink = ruleLink;
        this.path = Optional.of(path);
        this.lineNumber = Optional.of(lineNumber);
        this.columnNumber = Optional.of(columnNumber);
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

    public Optional<String> getPath() {
        return path;
    }

    public Optional<Integer> getLineNumber() {
        return lineNumber;
    }

    public Optional<Integer> getColumnNumber() {
        return columnNumber;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Violation)) return false;
        if (!super.equals(object)) return false;

        Violation violation = (Violation) object;

        if (!title.equals(violation.title)) return false;
        if (!description.equals(violation.description)) return false;
        if (!violationType.equals(violation.violationType)) return false;
        if (!ruleLink.equals(violation.ruleLink)) return false;
        if (!path.equals(violation.path)) return false;
        if (!lineNumber.equals(violation.lineNumber)) return false;
        if (!columnNumber.equals(violation.columnNumber)) return false;

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + violationType.hashCode();
        result = 31 * result + ruleLink.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + lineNumber.hashCode();
        result = 31 * result + columnNumber.hashCode();
        return result;
    }
}
