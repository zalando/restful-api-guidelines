package de.zalando.zally;

import java.util.Optional;

public class Violation {

    private final String title;
    private final String description;
    private final ViolationType violationType;
    private final String ruleLink;
    private final Optional<Integer> lineNumber;
    private final Optional<Integer> columnNumber;

    public Violation(String title, String description,
                     ViolationType violationType, String ruleLink) {
        this.title = title;
        this.description = description;
        this.violationType = violationType;
        this.ruleLink = ruleLink;
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

    public Optional<Integer> getLineNumber() {
        return lineNumber;
    }

    public Optional<Integer> getColumnNumber() {
        return columnNumber;
    }
}
