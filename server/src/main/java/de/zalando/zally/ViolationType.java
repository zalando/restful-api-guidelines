package de.zalando.zally;

public enum ViolationType {

    MUST("must"),
    SHOULD("should"),
    COULD("could"),     // DEPRECATED, see #326
    MAY("may"),
    HINT("hint");

    private final String metricIdentifier;

    ViolationType(String metricIdentifier) {
        this.metricIdentifier = metricIdentifier;
    }
}
