package de.zalando.zally.cli;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import de.zalando.zally.cli.api.RequestWrapperStrategy;
import de.zalando.zally.cli.api.UrlWrapperStrategy;
import de.zalando.zally.cli.api.ViolationsApiResponse;
import de.zalando.zally.cli.api.ZallyApiClient;
import de.zalando.zally.cli.domain.Violation;
import de.zalando.zally.cli.domain.ViolationType;
import java.io.IOException;
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


public class LinterTest {

    @Captor
    private ArgumentCaptor<List<Violation>> mustListCaptor;

    @Captor
    private ArgumentCaptor<List<Violation>> shouldListCaptor;

    @Captor
    private ArgumentCaptor<List<Violation>> mayListCaptor;

    @Captor
    private ArgumentCaptor<List<Violation>> hintListCaptor;

    @Mock
    private ZallyApiClient client;

    @Mock
    private ResultPrinter resultPrinter;

    @InjectMocks
    private Linter linter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void returnsTrueWhenNoViolationsAreReturned() throws Exception {
        final JSONObject testResult = getTestResult(new JSONArray());

        Boolean result = makeLinterCall(testResult);
        assertEquals(true, result);

        assertEquals(0, mustListCaptor.getAllValues().get(0).size());
        assertEquals(0, shouldListCaptor.getAllValues().get(0).size());
        assertEquals(0, mayListCaptor.getAllValues().get(0).size());
        assertEquals(0, hintListCaptor.getAllValues().get(0).size());
    }

    @Test
    public void returnsTrueWhenOnlyShouldAndMayViolationFound() throws Exception {
        final JSONArray violations = new JSONArray();
        violations.put(getViolation("SHOULD", "SHOULD"));
        violations.put(getViolation("MAY", "MAY"));
        violations.put(getViolation("HINT", "HINT"));
        final JSONObject testResult = getTestResult(violations);

        Boolean result = makeLinterCall(testResult);
        assertEquals(true, result);

        final List<Violation> shouldList = shouldListCaptor.getAllValues().get(0);
        final List<Violation> mayList = mayListCaptor.getAllValues().get(0);
        final List<Violation> hintList = hintListCaptor.getAllValues().get(0);

        assertEquals(0, mustListCaptor.getAllValues().get(0).size());
        assertEquals(1, shouldList.size());
        assertEquals(1, mayList.size());
        assertEquals(1, hintList.size());
        assertEquals("SHOULD", shouldList.get(0).getTitle());
        assertEquals("MAY", mayList.get(0).getTitle());
        assertEquals("HINT", hintList.get(0).getTitle());
    }

    @Test
    public void returnsFalseWhenMustViolationsFound() throws Exception {
        final JSONArray violations = new JSONArray();
        violations.put(getViolation("must", "must"));
        final JSONObject testResult = getTestResult(violations);

        Boolean result = makeLinterCall(testResult);
        assertEquals(false, result);

        List<Violation> mustList = mustListCaptor.getAllValues().get(0);

        assertEquals(1, mustList.size());
        assertEquals(0, shouldListCaptor.getAllValues().get(0).size());
        assertEquals(0, mayListCaptor.getAllValues().get(0).size());
        assertEquals(0, hintListCaptor.getAllValues().get(0).size());
        assertEquals("must", mustList.get(0).getTitle());
    }

    @Test
    public void printsMessageWhenSpecified() throws Exception {
        final JSONObject testResult = getTestResult(new JSONArray());
        final String message = "Test message";
        testResult.put("message", message);

        Mockito.when(client.validate(anyString())).thenReturn(new ViolationsApiResponse(testResult));
        linter = new Linter(client, resultPrinter);
        final boolean result = linter.lint(getUrlWrapperStrategy());
        assertEquals(result, true);

        Mockito.verify(resultPrinter, Mockito.times(1)).printMessage(eq(message));
    }

    private RequestWrapperStrategy getUrlWrapperStrategy() {
        final String url = "https://example.com/test.yaml";
        return new UrlWrapperStrategy(url);
    }

    private JSONObject getViolation(String title, String type) {
        JSONObject violation = new JSONObject();
        violation.put("title", title);
        violation.put("description", "Test Description: " + title);
        violation.put("violation_type", type);
        return violation;
    }

    private Boolean makeLinterCall(JSONObject testResult) throws IOException {
        Mockito.when(client.validate(anyString())).thenReturn(new ViolationsApiResponse(testResult));

        linter = new Linter(client, resultPrinter);
        final Boolean result = linter.lint(getUrlWrapperStrategy());

        Mockito.verify(
                resultPrinter,
                Mockito.times(1)).printSummary(any()
        );
        Mockito.verify(
                resultPrinter,
                Mockito.times(1)).printViolations(mustListCaptor.capture(), eq(ViolationType.MUST)
        );
        Mockito.verify(
                resultPrinter,
                Mockito.times(1)).printViolations(shouldListCaptor.capture(), eq(ViolationType.SHOULD)
        );
        Mockito.verify(
                resultPrinter,
                Mockito.times(1)).printViolations(mayListCaptor.capture(), eq(ViolationType.MAY)
        );
        Mockito.verify(
                resultPrinter,
                Mockito.times(1)).printViolations(hintListCaptor.capture(), eq(ViolationType.HINT)
        );

        return result;
    }

    private JSONObject getTestResult(JSONArray violations) {
        final JSONObject testResult = new JSONObject();
        testResult.put("violations", violations);
        testResult.put("violations_count", new JSONObject());
        return testResult;
    }
}
