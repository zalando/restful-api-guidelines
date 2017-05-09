package de.zalando.zally.cli;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestUtils {
    public static JSONObject createJsonRule(final String title, final String code) {
        final JSONObject rule = new JSONObject();
        rule.put("title", title);
        rule.put("code", code);
        rule.put("type", "MUST");
        rule.put("is_active", true);
        rule.put("url", "https://example.com");
        return rule;
    }

    public static JSONObject createJsonRuleApiResponse(int numberOfRules) {
        JSONArray rulesJson = new JSONArray();
        for (int i = 0; i < numberOfRules; i++) {
            rulesJson.put(
                    TestUtils.createJsonRule(
                            String.format("Test Rule %d", i),
                            String.format("M%03d", i)
                    )
            );
        }

        JSONObject apiResponse = new JSONObject();
        apiResponse.put("rules", rulesJson);

        return apiResponse;
    }
}
