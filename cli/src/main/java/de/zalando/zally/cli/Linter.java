package de.zalando.zally.cli;

import de.zalando.zally.cli.api.RequestWrapperStrategy;
import de.zalando.zally.cli.api.ViolationsApiResponse;
import de.zalando.zally.cli.api.ZallyApiClient;
import de.zalando.zally.cli.domain.Violation;
import de.zalando.zally.cli.domain.ViolationType;
import de.zalando.zally.cli.domain.ViolationsCount;
import de.zalando.zally.cli.exception.CliException;
import de.zalando.zally.cli.exception.CliExceptionType;
import java.io.IOException;
import java.util.List;


public class Linter {
    private final ZallyApiClient client;
    private final ResultPrinter printer;

    public Linter(ZallyApiClient client, ResultPrinter printer) {
        this.client = client;
        this.printer = printer;
    }

    public boolean lint(RequestWrapperStrategy requestWrapper) throws CliException {
        final ViolationsApiResponse response = client.validate(requestWrapper.wrap());
        final ViolationsFilter violationsFilter = new ViolationsFilter(response.getViolations());

        printMessage(response.getMessage());


        boolean hasMustViolations = false;
        for (ViolationType violationType : ViolationType.values()) {
            List<Violation> violations = violationsFilter.getViolations(violationType);
            if (ViolationType.MUST.equals(violationType)) {
                hasMustViolations = violations.isEmpty();
            }
            printViolations(violations, violationType);
        }

        printSummary(response.getCounters());

        return hasMustViolations;
    }

    private void printMessage(final String message) throws CliException {
        if (message == null) {
            return;
        }

        try {
            printer.printMessage(message);
        } catch (IOException exception) {
            throw new CliException(CliExceptionType.CLI, "Cannot print message:", exception.getMessage());
        }
    }

    private void printViolations(
            final List<Violation> violations, final ViolationType violationType) throws CliException {

        try {
            printer.printViolations(violations, violationType);
        }  catch (IOException exception) {
            throw new CliException(CliExceptionType.CLI, "Cannot print violations:", exception.getMessage());
        }
    }

    private void printSummary(final ViolationsCount counters) throws CliException {
        try {
            printer.printSummary(counters);
        } catch (IOException exception) {
            throw new CliException(CliExceptionType.CLI, "Cannot print violations summary:", exception.getMessage());
        }
    }
}

