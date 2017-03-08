package de.zalando.zally.cli;

import static org.junit.Assert.assertEquals;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;


public class ResultPrinterTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream outStream = new PrintStream(outContent);

    @Test
    public void testNoViolationsCase() throws IOException {
        ResultPrinter violationPrinter = new ResultPrinter(outStream);
        List<JsonObject> violations = new ArrayList<>();
        violationPrinter.printViolations(violations, "must");

        assertEquals("", outContent.toString());
    }

    @Test
    public void testWithViolationsCase() throws IOException {
        JsonObject violationOne = new JsonObject();
        violationOne.add("title", "Violation 1");
        violationOne.add("description", "Violation 1 Description");
        violationOne.add("paths", Json.array("Violation 1 Path"));
        violationOne.add("rule_link", Json.NULL);

        JsonObject violationTwo = new JsonObject();
        violationTwo.add("title", "Violation 2");
        violationTwo.add("description", "Violation 2 Description");
        violationTwo.add("paths", Json.array());
        violationTwo.add("rule_link", Json.NULL);

        List<JsonObject> violations = new ArrayList<>();
        violations.add(violationOne);
        violations.add(violationTwo);

        ResultPrinter violationPrinter = new ResultPrinter(outStream);
        violationPrinter.printViolations(violations, "must");

        String expectedResult =  violationPrinter.ANSI_RED + "\nFound the following MUST violations\n"
                + "===================================\n\n" + violationPrinter.ANSI_RESET
                + violationPrinter.ANSI_RED + "Violation 1\n" + violationPrinter.ANSI_RESET
                + "\tViolation 1 Description\n"
                + "\tViolated at:\n\t\tViolation 1 Path\n\n\n"
                + violationPrinter.ANSI_RED + "Violation 2\n" + violationPrinter.ANSI_RESET
                + "\tViolation 2 Description\n\n";

        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    public void printsProperSummary() throws IOException {
        final JsonObject counters = new JsonObject();
        counters.add("must", 12);
        counters.add("could", 13);
        counters.add("should", 14);
        counters.add("hint", 15);

        final ResultPrinter resultPrinter = new ResultPrinter(outStream);
        resultPrinter.printSummary(Linter.violationTypes, counters);

        String expectedResult = resultPrinter.ANSI_CYAN + "\nSummary:\n"
                + "========\n\n" + resultPrinter.ANSI_RESET
                + "MUST violations: 12\n"
                + "SHOULD violations: 14\n"
                + "COULD violations: 13\n"
                + "HINT violations: 15\n";
        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    public void formatReturnsProperlyColoredString() {
        JsonObject violation = new JsonObject();
        violation.add("title", "Test title");
        violation.add("description", "Test description");
        violation.add("rule_link", Json.NULL);
        violation.add("paths", Json.array());

        String[] testColors = new String[] {
                ResultPrinter.ANSI_RED,
                ResultPrinter.ANSI_YELLOW,
                ResultPrinter.ANSI_GREEN,
                ResultPrinter.ANSI_CYAN
        };

        for (String testColor : testColors) {
            String result = ResultPrinter.formatViolation(testColor, violation);
            assertEquals(testColor + "Test title\n" + ResultPrinter.ANSI_RESET + "\tTest description\n", result);
        }

    }

    @Test
    public void formatReturnsFullViolation() {
        JsonObject violation = new JsonObject();
        violation.add("title", "Test title");
        violation.add("description", "Test description");
        violation.add(
                "rule_link",
                "https://zalando.github.io/restful-api-guidelines/security/Security.html#must-secure-endpoints-with-oauth-20");
        violation.add("paths", Json.array("/products/{product_id}/"));

        String testColor = ResultPrinter.ANSI_RED;
        String expectedResult = testColor + "Test title\n" + ResultPrinter.ANSI_RESET
                + "\tTest description\n"
                + "\t" + ResultPrinter.ANSI_CYAN + "https://zalando.github.io/restful-api-guidelines/security/Security.html#must-secure-endpoints-with-oauth-20\n" + ResultPrinter.ANSI_RESET
                + "\tViolated at:\n\t\t/products/{product_id}/\n\n";

        String result = ResultPrinter.formatViolation(testColor, violation);
        assertEquals(expectedResult, result);
    }

    @Test
    public void printsProperMessage() throws Exception {
        final String message = "Test message";
        ResultPrinter resultPrinter = new ResultPrinter(outStream);
        resultPrinter.printMessage(message);

        final String expectedResult = resultPrinter.ANSI_CYAN
                + "\nServer message"
                + "\n==============\n\n"
                + resultPrinter.ANSI_RESET
                + message
                + "\n\n";

        assertEquals(expectedResult, outContent.toString());

    }
}
