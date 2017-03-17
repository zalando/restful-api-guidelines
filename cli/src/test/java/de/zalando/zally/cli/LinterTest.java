package de.zalando.zally.cli;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import de.zalando.zally.cli.api.ZallyApiClient;
import de.zalando.zally.cli.api.ZallyApiResponse;
import de.zalando.zally.cli.domain.Violation;
import de.zalando.zally.cli.reader.JsonReader;
import de.zalando.zally.cli.reader.SpecsReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private ArgumentCaptor<List<Violation>> couldListCaptor;

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
        assertEquals(0, couldListCaptor.getAllValues().get(0).size());
        assertEquals(0, hintListCaptor.getAllValues().get(0).size());
    }

    @Test
    public void returnsTrueWhenOnlyShouldAndCouldViolationFound() throws Exception {
        final JSONArray violations = new JSONArray();
        violations.put(getViolation("should", "should"));
        violations.put(getViolation("could", "could"));
        violations.put(getViolation("hint", "hint"));
        final JSONObject testResult = getTestResult(violations);

        Boolean result = makeLinterCall(testResult);
        assertEquals(true, result);

        final List<Violation> shouldList = shouldListCaptor.getAllValues().get(0);
        final List<Violation> couldList = couldListCaptor.getAllValues().get(0);
        final List<Violation> hintList = hintListCaptor.getAllValues().get(0);

        assertEquals(0, mustListCaptor.getAllValues().get(0).size());
        assertEquals(1, shouldList.size());
        assertEquals(1, couldList.size());
        assertEquals(1, hintList.size());
        assertEquals("should", shouldList.get(0).getTitle());
        assertEquals("could", couldList.get(0).getTitle());
        assertEquals("hint", hintList.get(0).getTitle());
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
        assertEquals(0, couldListCaptor.getAllValues().get(0).size());
        assertEquals(0, hintListCaptor.getAllValues().get(0).size());
        assertEquals("must", mustList.get(0).getTitle());
    }

    @Test
    public void printsMessageWhenSpecified() throws Exception {
        final JSONObject testResult = getTestResult(new JSONArray());
        final String message = "Test message";
        testResult.put("message", message);

        Mockito.when(client.validate(anyString())).thenReturn(new ZallyApiResponse(testResult));
        linter = new Linter(client, resultPrinter);
        final boolean result = linter.lint(getJsonReader());
        assertEquals(result, true);

        Mockito.verify(resultPrinter, Mockito.times(1)).printMessage(eq(message));
    }

    private SpecsReader getJsonReader() {
        String fixture = "{\"hello\":\"world\"}";
        InputStream inputStream = new ByteArrayInputStream(fixture.getBytes());
        return new JsonReader(new InputStreamReader(inputStream));
    }

    private JSONObject getViolation(String title, String type) {
        JSONObject violation = new JSONObject();
        violation.put("title", title);
        violation.put("description", "Test Description: " + title);
        violation.put("violation_type", type);
        return violation;
    }

    private Boolean makeLinterCall(JSONObject testResult) throws IOException {
        Mockito.when(client.validate(anyString())).thenReturn(new ZallyApiResponse(testResult));

        linter = new Linter(client, resultPrinter);
        final Boolean result = linter.lint(getJsonReader());

        Mockito.verify(resultPrinter, Mockito.times(1)).printSummary(eq(linter.violationTypes), any());
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(mustListCaptor.capture(), eq("must"));
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(shouldListCaptor.capture(), eq("should"));
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(couldListCaptor.capture(), eq("could"));
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(hintListCaptor.capture(), eq("hint"));

        return result;
    }

    private JSONObject getTestResult(JSONArray violations) {
        final JSONObject testResult = new JSONObject();
        testResult.put("violations", violations);
        testResult.put("violations_count", new JSONObject());
        return testResult;
    }
}
