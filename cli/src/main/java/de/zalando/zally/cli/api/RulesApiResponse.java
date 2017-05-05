package de.zalando.zally.cli.api;

import de.zalando.zally.cli.domain.Rule;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RulesApiResponse {
    private final JSONObject response;

    public RulesApiResponse(final JSONObject response) {
        this.response = response;
    }

    public List<Rule> getRules() {
        final JSONArray ruleJsons = response.getJSONArray("rules");
        List<Rule> rules = new ArrayList<>();

        for (int i = 0; i < ruleJsons.length(); i++) {
            rules.add(new Rule(ruleJsons.getJSONObject(i)));
        }

        return rules;
    }
}
