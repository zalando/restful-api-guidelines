package de.zalando.zally.cli;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

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
    private ArgumentCaptor<List<JsonObject>> mustListCaptor;

    @Captor
    private ArgumentCaptor<List<JsonObject>> shouldListCaptor;

    @Captor
    private ArgumentCaptor<List<JsonObject>> couldListCaptor;

    @Captor
    private ArgumentCaptor<List<JsonObject>> hintListCaptor;

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
        JsonObject testResult = new JsonObject();
        JsonArray violations = new JsonArray();
        testResult.add("violations", violations);

        Boolean result = makeLinterCall(testResult);
        assertEquals(true, result);

        assertEquals(0, mustListCaptor.getAllValues().get(0).size());
        assertEquals(0, shouldListCaptor.getAllValues().get(0).size());
        assertEquals(0, couldListCaptor.getAllValues().get(0).size());
        assertEquals(0, hintListCaptor.getAllValues().get(0).size());
    }

    @Test
    public void returnsTrueWhenOnlyShouldAndCouldViolationFound() throws Exception {
        final JsonObject testResult = new JsonObject();
        final JsonArray violations = new JsonArray();
        violations.add(getViolation("should", "should"));
        violations.add(getViolation("could", "could"));
        violations.add(getViolation("hint", "hint"));
        testResult.add("violations", violations);


        Boolean result = makeLinterCall(testResult);
        assertEquals(true, result);

        final List<JsonObject> shouldList = shouldListCaptor.getAllValues().get(0);
        final List<JsonObject> couldList = couldListCaptor.getAllValues().get(0);
        final List<JsonObject> hintList = hintListCaptor.getAllValues().get(0);

        assertEquals(0, mustListCaptor.getAllValues().get(0).size());
        assertEquals(1, shouldList.size());
        assertEquals(1, couldList.size());
        assertEquals(1, hintList.size());
        assertEquals("should", shouldList.get(0).get("title").asString());
        assertEquals("could", couldList.get(0).get("title").asString());
        assertEquals("hint", hintList.get(0).get("title").asString());
    }

    @Test
    public void returnsFalseWhenMustViolationsFound() throws Exception {
        JsonObject testResult = new JsonObject();
        JsonArray violations = new JsonArray();
        violations.add(getViolation("must", "must"));
        testResult.add("violations", violations);

        Boolean result = makeLinterCall(testResult);
        assertEquals(false, result);

        List<JsonObject> mustList = mustListCaptor.getAllValues().get(0);

        assertEquals(1, mustList.size());
        assertEquals(0, shouldListCaptor.getAllValues().get(0).size());
        assertEquals(0, couldListCaptor.getAllValues().get(0).size());
        assertEquals(0, hintListCaptor.getAllValues().get(0).size());
        assertEquals("must", mustList.get(0).get("title").asString());
    }

    @Test
    public void printsMessageWhenSpecified() throws Exception {
        final JsonObject testResult = new JsonObject();
        final String message = "Test message";
        testResult.add("violations", new JsonArray());
        testResult.add("message", message);

        Mockito.when(client.validate(anyString())).thenReturn(testResult);
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

    private JsonObject getViolation(String title, String type) {
        JsonObject violation = new JsonObject();
        violation.add("title", title);
        violation.add("description", "Test Description: " + title);
        violation.add("violation_type", type);
        return violation;
    }

    private Boolean makeLinterCall(JsonObject testResult) throws IOException {
        Mockito.when(client.validate(anyString())).thenReturn(testResult);

        linter = new Linter(client, resultPrinter);
        final Boolean result = linter.lint(getJsonReader());

        Mockito.verify(resultPrinter, Mockito.times(1)).printSummary(eq(linter.violationTypes));
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(mustListCaptor.capture(), eq("must"));
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(shouldListCaptor.capture(), eq("should"));
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(couldListCaptor.capture(), eq("could"));
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(hintListCaptor.capture(), eq("hint"));

        return result;
    }
}
