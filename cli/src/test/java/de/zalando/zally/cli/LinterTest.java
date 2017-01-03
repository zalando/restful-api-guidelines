package de.zalando.zally.cli;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

public class LinterTest {

    private ArgumentCaptor<List<JsonObject>> mustListCaptor = ArgumentCaptor.forClass(List.class);
    private ArgumentCaptor<List<JsonObject>> shouldListCaptor = ArgumentCaptor.forClass(List.class);
    private ArgumentCaptor<List<JsonObject>> couldListCaptor = ArgumentCaptor.forClass(List.class);

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

        Mockito.when(client.validate(anyString())).thenReturn(testResult);

        linter = new Linter(client, resultPrinter);
        Boolean result = linter.lint(getJsonReader());

        assertEquals(true, result);

        Mockito.verify(resultPrinter).printSummary();
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(mustListCaptor.capture(), eq("must"));
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(shouldListCaptor.capture(), eq("should"));
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(couldListCaptor.capture(), eq("could"));

        assertEquals(0, mustListCaptor.getAllValues().get(0).size());
        assertEquals(0, shouldListCaptor.getAllValues().get(0).size());
        assertEquals(0, couldListCaptor.getAllValues().get(0).size());
    }

    @Test
    public void returnsTrueWhenOnlyShouldAndCouldViolationFound() throws Exception {
        JsonObject testResult = new JsonObject();
        JsonArray violations = new JsonArray();
        violations.add(getViolation("should", "should"));
        violations.add(getViolation("could", "could"));
        testResult.add("violations", violations);

        Mockito.when(client.validate(anyString())).thenReturn(testResult);

        linter = new Linter(client, resultPrinter);
        Boolean result = linter.lint(getJsonReader());

        assertEquals(true, result);

        Mockito.verify(resultPrinter).printSummary();
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(mustListCaptor.capture(), eq("must"));
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(shouldListCaptor.capture(), eq("should"));
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(couldListCaptor.capture(), eq("could"));

        List<JsonObject> shouldList = shouldListCaptor.getAllValues().get(0);
        List<JsonObject> couldList = couldListCaptor.getAllValues().get(0);

        assertEquals(0, mustListCaptor.getAllValues().get(0).size());
        assertEquals(1, shouldList.size());
        assertEquals(1, couldList.size());
        assertEquals("should", shouldList.get(0).get("title").asString());
        assertEquals("could", couldList.get(0).get("title").asString());
    }

    @Test
    public void returnsFalseWhenMustViolationsFound() throws Exception {
        JsonObject testResult = new JsonObject();
        JsonArray violations = new JsonArray();
        violations.add(getViolation("must", "must"));
        testResult.add("violations", violations);

        Mockito.when(client.validate(anyString())).thenReturn(testResult);

        linter = new Linter(client, resultPrinter);
        Boolean result = linter.lint(getJsonReader());

        assertEquals(false, result);

        Mockito.verify(resultPrinter).printSummary();
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(mustListCaptor.capture(), eq("must"));
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(shouldListCaptor.capture(), eq("should"));
        Mockito.verify(resultPrinter, Mockito.times(1)).printViolations(couldListCaptor.capture(), eq("could"));

        List<JsonObject> mustList = mustListCaptor.getAllValues().get(0);

        assertEquals(1, mustList.size());
        assertEquals(0, shouldListCaptor.getAllValues().get(0).size());
        assertEquals(0, couldListCaptor.getAllValues().get(0).size());
        assertEquals("must", mustList.get(0).get("title").asString());
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
}
