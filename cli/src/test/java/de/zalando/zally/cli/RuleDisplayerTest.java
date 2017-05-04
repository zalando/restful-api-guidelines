package de.zalando.zally.cli;

import de.zalando.zally.cli.api.ZallyApiClient;
import de.zalando.zally.cli.domain.Rule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
        final List<Rule> rules = getRules();

        makeDisplayCall(rules);

        assertEquals(rules, rulesCaptor.getValue());
    }

    private void makeDisplayCall(List<Rule> rules) throws IOException {
        Mockito.when(client.listRules()).thenReturn(rules);

        ruleDisplayer.display();

        Mockito.verify(resultPrinter, Mockito.times(1)).printRules(rulesCaptor.capture());
    }

    private List<Rule> getRules() {
        Rule firstRule = new Rule("First rule", "M001", "MUST");
        firstRule.setActive(true);
        firstRule.setUrl("https://example.com/first-rule");

        Rule secondRule = new Rule("Second rule", "S002", "SHOULD");
        secondRule.setActive(true);
        secondRule.setUrl("https://example.com/second-rule");

        List<Rule> rules = new ArrayList<>();
        rules.add(firstRule);
        rules.add(secondRule);

        return rules;
    }
}