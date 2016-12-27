package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.github.ryenus.rop.OptionParser;
import com.github.ryenus.rop.OptionParser.Option;

import java.io.IOException;
import java.util.List;

@OptionParser.Command(name = "zally", descriptions = "Lints the given swagger file using Zally service")
public class Main {

    private static final String DEFAULT_ZALLY_URL = "http://localhost:8080/api-violations";

    @Option(opt = {"-u", "--url"}, description = "ZALLY Service URL")
    private String url;

    @Option(opt = {"-t", "--token"}, description = "OAuth2 Security Token")
    private String token;

    public static void main(String[] args) {
        OptionParser parser = new OptionParser(Main.class);
        parser.parse(args);
    }

    void run(String[] args) {
        try {
            System.exit(lint(args));
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private int lint(String[] args) throws RuntimeException {
        if (args.length < 1) {
            throw new RuntimeException("Please provide a swagger file");
        }

        final SpecsReader specsReader = new SpecsReaderFactory().create(args[0]);

        final RequestDecorator decorator = new RequestDecorator(specsReader);
        final ZallyApiClient client = new ZallyApiClient(getZallyUrl(), getToken());

        JsonValue response = client.validate(decorator.getRequestBody());

        ViolationsFilter violationsFilter = new ViolationsFilter(response.asObject());
        List<JsonObject> mustViolations = violationsFilter.getMustViolations();
        List<JsonObject> shouldViolations = violationsFilter.getShouldViolations();
        List<JsonObject> couldViolations = violationsFilter.getCouldViolations();

        ResultPrinter printer = new ResultPrinter(System.out);
        try {
            printer.printViolations(mustViolations, "must");
            printer.printViolations(shouldViolations, "should");
            printer.printViolations(couldViolations, "could");
            printer.printSummary();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return mustViolations.isEmpty() ? 0 : 1;
    }

    private String getToken() {
        String token = System.getenv("TOKEN");
        if (this.token != null) {
            return this.token;
        } else if (token != null) {
            return token;
        }
        return "";
    }

    private String getZallyUrl() {
        String url = System.getenv("ZALLY_URL");
        if (this.url != null) {
            return this.url;
        } else if (url != null) {
            return url;
        }
        return DEFAULT_ZALLY_URL;
    }
}
