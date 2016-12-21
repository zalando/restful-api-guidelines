package de.zalando.zally.cli;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.io.IOException;

public class ViolationFormatterTest {
    @Test
    public void testNoViolationsCase() throws IOException {
        ViolationFormatter violationFormatter = new ViolationFormatter(System.out);
        JsonObject violationsWrapper = new JsonObject();
        JsonArray violations = new JsonArray();
        violationsWrapper.add("violations", violations);
        Integer status = violationFormatter.show(violationsWrapper);
        assert (status).equals(0);
    }

    @Test
    public void testWithViolationsCase() throws IOException {
        ViolationFormatter violationFormatter = new ViolationFormatter(System.out);
        JsonObject violationsWrapper = new JsonObject();
        JsonArray violations = new JsonArray();
        JsonObject violation = new JsonObject();
        violations.add(violation);
        violationsWrapper.add("violations", violations);
        Integer status = violationFormatter.show(violationsWrapper);
        assert (status).equals(1);
    }
}
