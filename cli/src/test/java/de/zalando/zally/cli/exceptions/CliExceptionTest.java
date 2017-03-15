package de.zalando.zally.cli.exceptions;

import static org.junit.Assert.assertEquals;

import de.zalando.zally.cli.exceptions.CliException;
import de.zalando.zally.cli.exceptions.CliExceptionType;
import org.junit.Test;

public class CliExceptionTest {
    @Test
    public void getMessageReturnsProperlyFormattedMessage() {
        CliException exception = new CliException(CliExceptionType.API, "Test title", "Test message");
        assertEquals(exception.getMessage(), "API: Test title\n\nTest message");

        exception = new CliException(CliExceptionType.CLI, "Test title", "Test message");
        assertEquals(exception.getMessage(), "Command-line Parameters: Test title\n\nTest message");
    }

    @Test
    public void getMessageReturnsNoDetailsWhenNotSpecified() {
        String[] detailsSet = new String[] {null, ""};

        for (String details : detailsSet) {
            CliException exception = new CliException(CliExceptionType.API, "Test title", details);
            assertEquals(exception.getMessage(), "API: Test title");

        }
    }
}