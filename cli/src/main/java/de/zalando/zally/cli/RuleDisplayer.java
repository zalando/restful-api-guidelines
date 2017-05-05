package de.zalando.zally.cli;

import de.zalando.zally.cli.api.ZallyApiClient;
import de.zalando.zally.cli.domain.Rule;
import de.zalando.zally.cli.exception.CliException;
import de.zalando.zally.cli.exception.CliExceptionType;

import java.io.IOException;
import java.util.List;

public class RuleDisplayer {
    private final ZallyApiClient apiClient;
    private final ResultPrinter printer;

    public RuleDisplayer(final ZallyApiClient apiClient, final ResultPrinter printer) {
        this.apiClient = apiClient;
        this.printer = printer;
    }

    public void display() throws CliException {
        List<Rule> rules = apiClient.listRules();
        printRules(rules);
    }

    private void printRules(final List<Rule> rules) throws CliException {
        try {
            printer.printRules(rules);
        } catch (IOException exception) {
            throw new CliException(CliExceptionType.CLI, "Cannot display rules:", exception.getMessage());
        }
    }
}
