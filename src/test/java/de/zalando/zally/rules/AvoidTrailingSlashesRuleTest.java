package de.zalando.zally.rules;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AvoidTrailingSlashesRuleTest {

    @Test
    public void avoidTrailingSlashesRule() {
        assertThat(new AvoidTrailingSlashesRule().validate(null)).isEmpty();
    }
}
