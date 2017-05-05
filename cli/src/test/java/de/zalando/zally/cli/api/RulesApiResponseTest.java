package de.zalando.zally.cli.api;

import de.zalando.zally.cli.domain.Rule;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;


public class RulesApiResponseTest {
    @Test
    public void shouldTransformRulesFromJson() throws Exception {
        JSONArray rulesJson = new JSONArray();
        rulesJson.put(createJsonRule("First Rule", "M001"));
        rulesJson.put(createJsonRule("Second Rule", "M002"));

        JSONObject apiResponse = new JSONObject();
        apiResponse.put("rules", rulesJson);

        final RulesApiResponse response = new RulesApiResponse(apiResponse);

        final List<Rule> rules = response.getRules();
        final List<String> ruleTitles = rules.stream().map(Rule::getTitle).collect(Collectors.toList());

        Assert.assertEquals(2, rules.size());
        Assert.assertTrue(ruleTitles.contains("First Rule"));
        Assert.assertTrue(ruleTitles.contains("Second Rule"));
    }

    private JSONObject createJsonRule(final String title, final String code) {
        final JSONObject rule = new JSONObject();
        rule.put("title", title);
        rule.put("code", code);
        rule.put("type", "MUST");
        rule.put("is_active", true);
        rule.put("url", "https://example.com");
        return rule;
    }
}