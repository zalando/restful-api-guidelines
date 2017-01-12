package de.zalando.zally.cli;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


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
        ResultPrinter violationPrinter = new ResultPrinter(outStream);
        List<JsonObject> violations = new ArrayList<>();

        JsonObject violationOne = new JsonObject();
        violationOne.add("title", "Violation 1");
        violationOne.add("description", "Violation 1 Description");
        violationOne.add("path", "Violation 1 Path");

        JsonObject violationTwo = new JsonObject();
        violationTwo.add("title", "Violation 2");
        violationTwo.add("description", "Violation 2 Description");
        violationTwo.add("path", Json.NULL);

        violations.add(violationOne);
        violations.add(violationTwo);
        violationPrinter.printViolations(violations, "must");

        String expectedResult =  violationPrinter.ANSI_YELLOW + "\nFound the following MUST violations\n" +
                "===================================\n" + violationPrinter.ANSI_RESET +
                "Violation 1:\n\tViolation 1 Description\n\tViolation 1 Path\n" +
                "Violation 2:\n\tViolation 2 Description\n";

        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    public void printsProperSummary() throws IOException {
        JsonObject violationOne = new JsonObject();
        violationOne.add("title", "Violation 1");
        violationOne.add("description", "Violation 1 Description");
        violationOne.add("path", "Violation 1 Path");

        List<JsonObject> violations = new ArrayList<>();
        violations.add(violationOne);

        ResultPrinter resultPrinter = new ResultPrinter(outStream);
        resultPrinter.printViolations(violations, "must");
        resultPrinter.printViolations(violations, "should");

        outContent.reset();

        resultPrinter.printSummary();
        String expectedResult = resultPrinter.ANSI_GREEN + "\nSummary:\n" +
                "========\n" + resultPrinter.ANSI_RESET +
                "SHOULD violations: 1\n" +
                "MUST violations: 1\n";
        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    public void formatReturnsProperlyFormattedString() {
        JsonObject violation = new JsonObject();
        violation.add("title", "Test title");
        violation.add("description", "Test description");
        violation.add("path", Json.NULL);

        String result = ResultPrinter.formatViolation(violation);

        assertEquals("Test title:\n\tTest description", result);
    }
}
