package de.zalando.zally.cli;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import com.github.ryenus.rop.OptionParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;


@OptionParser.Command(name = "zally", descriptions = "Lints the given swagger file using Zally service")
public class Main {
    public static void main(String[] args) {
        OptionParser parser = new OptionParser(Main.class);
        parser.parse(args);
    }

    void run(String[] args) {
        try {
            lint(args);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private void lint(String[] args) throws RuntimeException {
        if (args.length < 1) {
            throw new RuntimeException("Please provide a swagger file");
        }

        final SpecsReader specsReader;
        if (args[0].endsWith(".yaml") || args[0].endsWith(".yml")) {
            specsReader = new YamlReader(getReader(args[0]));
        } else {
            specsReader = new JsonReader(getReader(args[0]));
        }

        final RequestDecorator decorator = new RequestDecorator(specsReader);
        final String body = decorator.getRequestBody();

        JsonValue response = sendRequest(body);

        System.out.println(response.toString());
    }

    private Reader getReader(String location) throws RuntimeException {
        try {
            return new FileReader(location);
        } catch (FileNotFoundException e) {
            String message = "File " + location + " is not found";
            throw new RuntimeException(message);
        }
    }

    private JsonValue sendRequest(String body) throws RuntimeException {
        HttpResponse<String> response;

        // TODO: Override the URL from CLI args
        String url= System.getenv("ZALLY_URL");
        String token = System.getenv("TOKEN");

        try {
            response = Unirest
                    .post(url)
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asString();
        } catch (UnirestException e) {
            throw new RuntimeException("API Error: " + e.getMessage());
        }

        return Json.parse(response.getBody());
    }
}
