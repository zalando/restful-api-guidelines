package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class Linter {
    public static final List<String> violationTypes = Arrays.asList("must", "should", "could", "hint");

    private final ZallyApiClient client;
    private final ResultPrinter printer;
    private final String mustViolationType = "must";

    public Linter(ZallyApiClient client, ResultPrinter printer) {
        this.client = client;
        this.printer = printer;
    }

    public boolean lint(SpecsReader reader) throws IOException, CliException {
        final RequestDecorator decorator = new RequestDecorator(reader);
        final JsonObject jsonResponse = client.validate(decorator.getRequestBody()).asObject();
        final ZallyApiResponse response = new ZallyApiResponse(jsonResponse);
        final ViolationsFilter violationsFilter = new ViolationsFilter(response.getViolations());

        if (jsonResponse.names().contains("message")) {
            printer.printMessage(jsonResponse.get("message").asString());
        }

        boolean hasMustViolations = false;
        for (String violationType : violationTypes) {
            List<Violation> violations = violationsFilter.getViolations(violationType);
            if (mustViolationType.equals(violationType)) {
                hasMustViolations = violations.isEmpty();
            }
            printer.printViolations(violations, violationType);
        }

        printer.printSummary(violationTypes, response.getCounters());

        return hasMustViolations;
    }
}

