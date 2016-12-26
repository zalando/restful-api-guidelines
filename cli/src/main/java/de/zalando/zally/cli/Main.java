package de.zalando.zally.cli;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.github.ryenus.rop.OptionParser;
import com.github.ryenus.rop.OptionParser.Option;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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
        final String body = decorator.getRequestBody();

        JsonValue response = sendRequest(body);

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

    private JsonValue sendRequest(String body) throws RuntimeException {
        HttpResponse<String> response;

        try {
            response = Unirest
                    .post(getZallyUrl())
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asString();
        } catch (UnirestException e) {
            throw new RuntimeException("API Error: " + e.getMessage());
        }

        return Json.parse(response.getBody());
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
