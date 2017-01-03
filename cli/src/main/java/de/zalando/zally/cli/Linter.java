package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.IOException;
import java.util.List;


public class Linter {
    private final ZallyApiClient client;
    private final ResultPrinter printer;

    public Linter(ZallyApiClient client, ResultPrinter printer) {
        this.client = client;
        this.printer = printer;
    }

    public boolean lint(SpecsReader reader) throws IOException {
        final RequestDecorator decorator = new RequestDecorator(reader);
        final JsonValue response = client.validate(decorator.getRequestBody());
        final ViolationsFilter violationsFilter = new ViolationsFilter(response.asObject());

        List<JsonObject> mustViolations = violationsFilter.getMustViolations();
        List<JsonObject> shouldViolations = violationsFilter.getShouldViolations();
        List<JsonObject> couldViolations = violationsFilter.getCouldViolations();

        printer.printViolations(mustViolations, "must");
        printer.printViolations(shouldViolations, "should");
        printer.printViolations(couldViolations, "could");

        printer.printSummary();

        return mustViolations.isEmpty();
    }
}

