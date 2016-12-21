package de.zalando.zally.cli;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class ViolationsPrinterTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream outStream = new PrintStream(outContent);

    @Test
    public void testNoViolationsCase() throws IOException {
        ViolationsPrinter violationPrinter = new ViolationsPrinter(outStream);
        List<String> violations = new ArrayList<>();
        violationPrinter.print(violations, "must");

        assertEquals("", outContent.toString());
    }

    @Test
    public void testWithViolationsCase() throws IOException {
        ViolationsPrinter violationPrinter = new ViolationsPrinter(outStream);
        List<String> violations = new ArrayList<>();
        violations.add("Violation 1");
        violations.add("Violation 2");
        violationPrinter.print(violations, "must");

        String expectedResult = "Found the following MUST violations\n" +
                "===================================\n" +
                "Violation 1\n" +
                "Violation 2\n";

        assertEquals(expectedResult, outContent.toString());
    }
}
