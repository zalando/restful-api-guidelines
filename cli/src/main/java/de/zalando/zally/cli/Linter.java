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

    public boolean lint(SpecsReader reader) throws IOException, CliException {
        final RequestDecorator decorator = new RequestDecorator(reader);
        final JsonValue response = client.validate(decorator.getRequestBody());
        final ViolationsFilter violationsFilter = new ViolationsFilter(response.asObject());

        List<JsonObject> mustViolations = violationsFilter.getViolations("MUST");
        List<JsonObject> shouldViolations = violationsFilter.getViolations("SHOULD");
        List<JsonObject> couldViolations = violationsFilter.getViolations("COULD");

        printer.printViolations(mustViolations, "must");
        printer.printViolations(shouldViolations, "should");
        printer.printViolations(couldViolations, "could");

        printer.printSummary();

        return mustViolations.isEmpty();
    }
}

