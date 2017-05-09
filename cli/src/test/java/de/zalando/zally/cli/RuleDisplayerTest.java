package de.zalando.zally.cli;

import static org.junit.Assert.assertEquals;

import de.zalando.zally.cli.api.RulesApiResponse;
import de.zalando.zally.cli.api.ZallyApiClient;
import de.zalando.zally.cli.domain.Rule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

        List<String> expectedTitles = expectedRules.stream().map(Rule::getTitle).collect(Collectors.toList());
        List<String> actualTitles = rulesCaptor.getValue().stream().map(Rule::getTitle).collect(Collectors.toList());

        assertEquals(expectedTitles, actualTitles);
    }

    private void makeDisplayCall(final JSONObject apiRules) throws IOException {
        Mockito.when(client.queryRules()).thenReturn(new RulesApiResponse(apiRules));

        ruleDisplayer.display();

        Mockito.verify(resultPrinter, Mockito.times(1)).printRules(rulesCaptor.capture());
    }

    private JSONObject getApiResponse() {
        return TestUtils.createJsonRuleApiResponse(2);
    }
}