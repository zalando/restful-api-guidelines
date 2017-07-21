package de.zalando.zally.dto;

public enum ViolationType {

    MUST("must"),
    SHOULD("should"),
    MAY("may"),
    HINT("hint"),

    /*
     * @deprecated Use MAY instead
     */
    @Deprecated COULD("could");

    private final String metricIdentifier;

    ViolationType(String metricIdentifier) {
        this.metricIdentifier = metricIdentifier;
    }
}
