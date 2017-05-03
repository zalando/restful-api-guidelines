package de.zalando.zally.cli.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.json.JSONObject;
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

    @Test
    public void shouldCreateRuleFromJson() throws Exception {
        final JSONObject ruleJson = new JSONObject();
        ruleJson.put("title", "Test title");
        ruleJson.put("code", "M001");
        ruleJson.put("type", "MUST");
        ruleJson.put("is_active", true);
        ruleJson.put("url", "https://example.com");

        final Rule rule = new Rule(ruleJson);

        assertEquals(rule.getTitle(), "Test title");
        assertEquals(rule.getCode(), "M001");
        assertEquals(rule.getType(), "MUST");
        assertEquals(rule.getActive(), true);
        assertEquals(rule.getUrl(), "https://example.com");
    }
}
