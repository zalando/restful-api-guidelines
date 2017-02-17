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
        violationOne.add("path", "Violation 1 Path");
        violationOne.add("rule_link", Json.NULL);

        JsonObject violationTwo = new JsonObject();
        violationTwo.add("title", "Violation 2");
        violationTwo.add("description", "Violation 2 Description");
        violationTwo.add("path", Json.NULL);
        violationTwo.add("rule_link", Json.NULL);

        List<JsonObject> violations = new ArrayList<>();
        violations.add(violationOne);
        violations.add(violationTwo);

        ResultPrinter violationPrinter = new ResultPrinter(outStream);
        violationPrinter.printViolations(violations, "must");

        String expectedResult =  violationPrinter.ANSI_RED + "\nFound the following MUST violations\n"
                + "===================================\n\n" + violationPrinter.ANSI_RESET
                + violationPrinter.ANSI_RED + "Violation 1\n" + violationPrinter.ANSI_RESET
                + "\t(path: Violation 1 Path)\n"
                + "\tViolation 1 Description\n\n"
                + violationPrinter.ANSI_RED + "Violation 2\n" + violationPrinter.ANSI_RESET
                + "\tViolation 2 Description\n\n";

        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    public void printsProperSummary() throws IOException {
        JsonObject violationOne = new JsonObject();
        violationOne.add("title", "Violation 1");
        violationOne.add("description", "Violation 1 Description");
        violationOne.add("path", "Violation 1 Path");
        violationOne.add("rule_link", Json.NULL);

        List<JsonObject> violations = new ArrayList<>();
        violations.add(violationOne);

        ResultPrinter resultPrinter = new ResultPrinter(outStream);
        resultPrinter.printViolations(violations, "must");
        resultPrinter.printViolations(violations, "should");

        outContent.reset();

        resultPrinter.printSummary(Linter.violationTypes);
        String expectedResult = resultPrinter.ANSI_WHITE + "\nSummary:\n"
                + "========\n\n" + resultPrinter.ANSI_RESET
                + "MUST violations: 1\n"
                + "SHOULD violations: 1\n"
                + "COULD violations: 0\n"
                + "HINT violations: 0\n";
        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    public void formatReturnsProperlyColoredString() {
        JsonObject violation = new JsonObject();
        violation.add("title", "Test title");
        violation.add("description", "Test description");
        violation.add("rule_link", Json.NULL);
        violation.add("path", Json.NULL);

        String[] testColors = new String[] {
                ResultPrinter.ANSI_RED,
                ResultPrinter.ANSI_YELLOW,
                ResultPrinter.ANSI_GREEN
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
        violation.add("path", "/products/{product_id}/");

        String testColor = ResultPrinter.ANSI_RED;
        String expectedResult = testColor + "Test title\n" + ResultPrinter.ANSI_RESET
                + "\t(path: /products/{product_id}/)\n"
                + "\tTest description\n"
                + "\t" + ResultPrinter.ANSI_CYAN
                + "https://zalando.github.io/restful-api-guidelines/security/Security.html#must-secure-endpoints-with-oauth-20"
                + "\n" + ResultPrinter.ANSI_RESET;

        String result = ResultPrinter.formatViolation(testColor, violation);
        assertEquals(expectedResult, result);
    }

    @Test
    public void printsProperMessage() throws Exception {
        final String message = "Test message";
        ResultPrinter resultPrinter = new ResultPrinter(outStream);
        resultPrinter.printMessage(message);

        final String expectedResult = resultPrinter.ANSI_WHITE
                + "\nServer message"
                + "\n==============\n\n"
                + resultPrinter.ANSI_RESET
                + message;

        assertEquals(expectedResult, outContent.toString());

    }
}
