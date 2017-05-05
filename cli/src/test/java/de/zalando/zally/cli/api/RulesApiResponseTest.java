package de.zalando.zally.cli.api;

import de.zalando.zally.cli.TestUtils;
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
        rulesJson.put(TestUtils.createJsonRule("First Rule", "M001"));
        rulesJson.put(TestUtils.createJsonRule("Second Rule", "M002"));

        JSONObject apiResponse = new JSONObject();
        apiResponse.put("rules", rulesJson);

        final RulesApiResponse response = new RulesApiResponse(apiResponse);

        final List<Rule> rules = response.getRules();
        final List<String> ruleTitles = rules.stream().map(Rule::getTitle).collect(Collectors.toList());

        Assert.assertEquals(2, rules.size());
        Assert.assertTrue(ruleTitles.contains("First Rule"));
        Assert.assertTrue(ruleTitles.contains("Second Rule"));
    }
}