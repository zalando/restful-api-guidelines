package de.zalando.zally.rules;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefineOAuthScopesRuleTest {

    @Test
    public void emptyAPI() {
        assertThat(new DefineOAuthScopesRule().validate(null)).isEmpty();
    }


}
