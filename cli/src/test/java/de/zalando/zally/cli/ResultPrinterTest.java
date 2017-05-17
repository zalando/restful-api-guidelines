package de.zalando.zally.cli;

import static org.junit.Assert.assertEquals;

import de.zalando.zally.cli.domain.Rule;
import de.zalando.zally.cli.domain.Violation;
import de.zalando.zally.cli.domain.ViolationType;
import de.zalando.zally.cli.domain.ViolationsCount;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;


public class ResultPrinterTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream outStream = new PrintStream(outContent);

    @Test
    public void testNoViolationsCase() throws IOException {
        ResultPrinter violationPrinter = new ResultPrinter(outStream);
        List<Violation> violations = new ArrayList<>();
        violationPrinter.printViolations(violations, ViolationType.MUST);

        assertEquals("", outContent.toString());
    }

    @Test
    public void testWithViolationsCase() throws IOException {
        final List<String> paths = new ArrayList<>();
        paths.add("Violation 1 Path");

        final Violation violationOne = new Violation("Violation 1", "Violation 1 Description");
        violationOne.setViolationType(ViolationType.MUST);
        violationOne.setPaths(paths);

        final Violation violationTwo = new Violation("Violation 2", "Violation 2 Description");
        violationTwo.setViolationType(ViolationType.MUST);


        final List<Violation> violations = new ArrayList<>();
        violations.add(violationOne);
        violations.add(violationTwo);

        ResultPrinter violationPrinter = new ResultPrinter(outStream);
        violationPrinter.printViolations(violations, ViolationType.MUST);

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
        final Map<String, Integer> counters = new HashMap<>();
        counters.put("must", 12);
        counters.put("should", 14);
        counters.put("may", 13);
        counters.put("hint", 15);

        final ResultPrinter resultPrinter = new ResultPrinter(outStream);
        resultPrinter.printSummary(new ViolationsCount(counters));

        String expectedResult = resultPrinter.ANSI_CYAN + "\nSummary:\n"
                + "========\n\n" + resultPrinter.ANSI_RESET
                + "MUST violations: 12\n"
                + "SHOULD violations: 14\n"
                + "MAY violations: 13\n"
                + "HINT violations: 15\n";
        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    public void printsRules() throws Exception {
        Rule firstRule = new Rule("First rule", "M001", "MUST");
        firstRule.setActive(true);
        firstRule.setUrl("https://example.com/first-rule");

        Rule secondRule = new Rule("Second rule", "S002", "SHOULD");
        secondRule.setActive(true);
        secondRule.setUrl("https://example.com/second-rule");

        List<Rule> rules = new ArrayList<>();
        rules.add(firstRule);
        rules.add(secondRule);

        final ResultPrinter resultPrinter = new ResultPrinter(outStream);
        resultPrinter.printRules(rules);

        final String expectedResult = ResultPrinter.ANSI_CYAN
                + "\nSupported Rules\n"
                + "===============\n"
                + "\n" + ResultPrinter.ANSI_RESET
                + ResultPrinter.ANSI_GREEN + "M001" + ResultPrinter.ANSI_RESET + " MUST First rule\n"
                + "\t(https://example.com/first-rule)\n"
                + ResultPrinter.ANSI_GREEN + "S002" + ResultPrinter.ANSI_RESET + " SHOULD Second rule\n"
                + "\t(https://example.com/second-rule)\n";
        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    public void formatReturnsProperlyColoredString() {
        Violation violation = new Violation("Test title", "Test description");

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
        final String ruleLink = "https://zalando.github.io/restful-api-guidelines/security/Security.html"
                + "#must-secure-endpoints-with-oauth-20";

        final List<String> paths = new ArrayList<>();
        paths.add("/path/one");
        paths.add("/path/two");

        final Violation violation = new Violation("Test title", "Test description");
        violation.setRuleLink(ruleLink);
        violation.setPaths(paths);

        String testColor = ResultPrinter.ANSI_RED;
        String expectedResult = testColor + "Test title\n" + ResultPrinter.ANSI_RESET
                + "\tTest description\n"
                + "\t" + ResultPrinter.ANSI_CYAN + ruleLink + "\n" + ResultPrinter.ANSI_RESET
                + "\tViolated at:\n\t\t/path/one\n\t\t/path/two\n\n";

        String result = ResultPrinter.formatViolation(testColor, violation);
        assertEquals(expectedResult, result);
    }

    @Test
    public void formatsActiveRule() {
        Rule rule = new Rule("Test title", "M001", "MUST");
        rule.setUrl("http://example.com/rule-description");
        rule.setActive(true);

        final String output = ResultPrinter.formatRule(rule);
        final String expectedOutput = ResultPrinter.ANSI_GREEN + "M001" + ResultPrinter.ANSI_RESET
                + " MUST Test title\n\t(http://example.com/rule-description)";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void formatsInactiveRule() {
        Rule rule = new Rule("Test title", "M001", "MUST");
        rule.setUrl("http://example.com/rule-description");
        rule.setActive(false);

        final String output = ResultPrinter.formatRule(rule);
        final String expectedOutput = ResultPrinter.ANSI_RED + "M001" + ResultPrinter.ANSI_RESET
                + " MUST Test title\n\t(http://example.com/rule-description)";
        assertEquals(expectedOutput, output);
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
