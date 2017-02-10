package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class Linter {
    private final ZallyApiClient client;
    private final ResultPrinter printer;
    private final List<String> violationTypes = Arrays.asList("must", "should", "could");
    private final String mustViolationType = "must";

    public Linter(ZallyApiClient client, ResultPrinter printer) {
        this.client = client;
        this.printer = printer;
    }

    public boolean lint(SpecsReader reader) throws IOException, CliException {
        final RequestDecorator decorator = new RequestDecorator(reader);
        final JsonValue response = client.validate(decorator.getRequestBody());
        final ViolationsFilter violationsFilter = new ViolationsFilter(response.asObject());

        boolean hasMustViolations = false;
        for (String violationType : violationTypes) {
            List<JsonObject> violations = violationsFilter.getViolations(violationType);
            if (mustViolationType.equals(violationType)) {
                hasMustViolations = violations.isEmpty();
            }
            printer.printViolations(violations, violationType);
        }

        printer.printSummary();

        return hasMustViolations;
    }
}

