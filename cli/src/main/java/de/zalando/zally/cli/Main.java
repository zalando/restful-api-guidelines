package de.zalando.zally.cli;

import com.github.ryenus.rop.OptionParser;
import com.github.ryenus.rop.OptionParser.Option;
import de.zalando.zally.cli.api.RequestWrapperFactory;
import de.zalando.zally.cli.api.RequestWrapperStrategy;
import de.zalando.zally.cli.api.ZallyApiClient;
import de.zalando.zally.cli.exception.CliException;
import de.zalando.zally.cli.exception.CliExceptionType;
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

    private void run(String[] args) {
        try {
            int exitCode = lint(args) ? 0 : 1;
            System.exit(exitCode);
        } catch (CliException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        }
    }

    private boolean lint(String[] args) throws CliException {
        if (args.length < 1) {
            System.err.println("Please provide a swagger file path or URL\n");
            parser.showHelp();
            throw new CliException();
        }

        final ZallyApiClient client = new ZallyApiClient(getZallyUrl(), getToken());
        final ResultPrinter printer = new ResultPrinter(System.out);
        final Linter linter = new Linter(client, printer);
        final RequestWrapperStrategy requestWrapper = new RequestWrapperFactory().create(args[0]);

        try {
            return linter.lint(requestWrapper);
        } catch (IOException exception) {
            throw new CliException(CliExceptionType.CLI, "Linter error:", exception.getMessage());
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
