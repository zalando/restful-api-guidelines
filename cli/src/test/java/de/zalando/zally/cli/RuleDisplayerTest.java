package de.zalando.zally.cli;

import static org.junit.Assert.assertEquals;

import de.zalando.zally.cli.api.RulesApiResponse;
import de.zalando.zally.cli.api.ZallyApiClient;
import de.zalando.zally.cli.domain.Rule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class RuleDisplayerTest {
    @Mock
    private ZallyApiClient client;

    @Mock
    private ResultPrinter resultPrinter;

    @InjectMocks
    private RuleDisplayer ruleDisplayer;

    @Captor
    private ArgumentCaptor<List<Rule>> rulesCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldDisplayRulesFromApi() throws Exception {
        final JSONObject apiResponse = getApiResponse();

        makeDisplayCall(apiResponse);

        JSONArray apiRules = apiResponse.getJSONArray("rules");
        List<Rule> expectedRules = new ArrayList<>();
        for (int i = 0; i < apiRules.length(); i++) {
            expectedRules.add(new Rule(apiRules.getJSONObject(i)));
        }

        assertEquals(expectedRules, rulesCaptor.getValue());
    }

    private void makeDisplayCall(final JSONObject apiRules) throws IOException {
        Mockito.when(client.queryRules()).thenReturn(new RulesApiResponse(apiRules));

        ruleDisplayer.display();

        Mockito.verify(resultPrinter, Mockito.times(1)).printRules(rulesCaptor.capture());
    }

    private JSONObject getApiResponse() {
        JSONArray rulesJson = new JSONArray();
        rulesJson.put(createJsonRule("First Rule", "M001"));
        rulesJson.put(createJsonRule("Second Rule", "M002"));

        JSONObject apiRules = new JSONObject();
        apiRules.put("rules", rulesJson);

        return apiRules;
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