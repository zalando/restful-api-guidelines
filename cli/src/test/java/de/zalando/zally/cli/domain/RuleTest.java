package de.zalando.zally.cli.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public class RuleTest {

    @Test
    public void shouldCreateRule() throws Exception {
        final Rule rule = new Rule("Test title", "M001", "MUST");

        assertEquals(rule.getTitle(), "Test title");
        assertEquals(rule.getCode(), "M001");
        assertEquals(rule.getType(), "MUST");
        assertNull(rule.getActive());
        assertNull(rule.getUrl());
    }
}
