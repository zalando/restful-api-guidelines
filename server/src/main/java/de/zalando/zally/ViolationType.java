package de.zalando.zally;

public enum ViolationType {

    MUST("must"),
    SHOULD("should"),
    COULD("could"),
    HINT("hint");

    private final String metricIdentifier;

    ViolationType(String metricIdentifier) {
        this.metricIdentifier = metricIdentifier;
    }

    public String getMetricIdentifier() {
        return metricIdentifier;
    }
}
