package de.zalando.zally.cli;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.io.IOException;

public class ShowViolationsTest {
    @Test
    public void testNoViolationsCase() throws IOException {
        ShowViolations showViolations = new ShowViolations(System.out);
        JsonObject violationsWrapper = new JsonObject();
        JsonArray violations = new JsonArray();
        violationsWrapper.add("violations", violations);
        Integer status = showViolations.show(violationsWrapper);
        assert (status).equals(0);
    }

    @Test
    public void testWithViolationsCase() throws IOException {
        ShowViolations showViolations = new ShowViolations(System.out);
        JsonObject violationsWrapper = new JsonObject();
        JsonArray violations = new JsonArray();
        JsonObject violation = new JsonObject();
        violations.add(violation);
        violationsWrapper.add("violations", violations);
        Integer status = showViolations.show(violationsWrapper);
        assert (status).equals(1);
    }
}
