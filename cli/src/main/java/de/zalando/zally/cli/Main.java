package de.zalando.zally.cli;

import com.github.ryenus.rop.OptionParser;
import com.github.ryenus.rop.OptionParser.Option;

import java.io.IOException;


@OptionParser.Command(name = "zally", descriptions = {
        "Validates the given swagger specification using Zally service",
        "\nUsage examples:",
        "\tzally /path/to/swagger.yml",
        "\tzally https://example.com/swagger.yml",
        "\tzally /path/to/swagger.yml -t abcd-ef12-3456-7890 -l http://zally.example.com/",
        }
)
public class Main {

    private static final String DEFAULT_ZALLY_URL = "http://localhost:8080/api-violations";

    @Option(opt = {"-l", "--linter-service"}, description = "ZALLY Service URL")
    private String url;

    @Option(opt = {"-t", "--token"}, description = "OAuth2 Security Token")
    private String token;

    private static OptionParser parser;

    public static void main(String[] args) {
        parser = new OptionParser(Main.class);
        try {
            parser.parse(args);
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        }
    }

    void run(String[] args) {
        try {
            int exitCode = lint(args) ? 0 : 1;
            System.exit(exitCode);
        } catch (RuntimeException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        }
    }

    private boolean lint(String[] args) throws RuntimeException {
        if (args.length < 1) {
            System.err.println("Please provide a swagger file path or URL\n");
            parser.showHelp();
            throw new RuntimeException("");
        }

        final ZallyApiClient client = new ZallyApiClient(getZallyUrl(), getToken());
        final ResultPrinter printer = new ResultPrinter(System.out);
        final SpecsReader specsReader = new SpecsReaderFactory().create(args[0]);

        Linter linter = new Linter(client, printer);
        try {
            return linter.lint(specsReader);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
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
