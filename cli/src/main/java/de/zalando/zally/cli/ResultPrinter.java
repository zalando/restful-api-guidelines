package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Display violations in a user friendly manner.
 */
public class ResultPrinter {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private final Writer writer;
    private final Map<String, Integer> counters = new HashMap<>();

    public ResultPrinter(OutputStream outputStream) {
        writer = new OutputStreamWriter(outputStream);
    }

    public void printMessage(String message) throws IOException {
        if (!message.isEmpty()) {
            printHeader(ANSI_CYAN, "Server message");
            writer.write(message + "\n\n");
            writer.flush();
        }
    }

    public void printViolations(List<JsonObject> violations, String violationType) throws IOException {
        if (!violations.isEmpty()) {

            String normalizedViolationType = violationType.toUpperCase();
            String header = String.format("Found the following %s violations", normalizedViolationType);
            String headerColor = getHeaderColor(normalizedViolationType);

            printHeader(headerColor, header);

            for (JsonObject violation : violations) {
                writer.write(formatViolation(headerColor, violation) + "\n");
            }
            writer.flush();
        }

        this.updateCounter(violationType, violations.size());
    }

    public void printSummary(List<String> violationTypeNames) throws IOException {
        printHeader(ANSI_CYAN, "Summary:");
        for (String name : violationTypeNames) {
            writer.write(name.toUpperCase() + " violations: " + counters.getOrDefault(name, 0).toString() + "\n");
        }
        writer.flush();
    }

    public void printHeader(String ansiColor, String header) throws IOException {
        String headerUnderline = new String(new char[header.length()]).replace("\0", "=");
        writer.write(ansiColor + "\n" + header + "\n" + headerUnderline + "\n\n" + ANSI_RESET);
        writer.flush();
    }

    public static String formatViolation(String headerColor, JsonObject violation) {
        String title = violation.get("title").asString();
        String description = violation.get("description").asString();
        String path = !violation.get("path").isNull() ? violation.get("path").asString() : "";

        StringBuilder sb = new StringBuilder();
        sb.append(headerColor + title + "\n" + ANSI_RESET);
        if (!path.isEmpty()) {
            sb.append("\t(path: " + path + ")\n");
        }
        sb.append("\t" + description + "\n");

        String ruleLink = !violation.get("rule_link").isNull() ? violation.get("rule_link").asString() : "";
        if (!ruleLink.isEmpty()) {
            sb.append("\t" + ANSI_CYAN + ruleLink + "\n" + ANSI_RESET);
        }

        return sb.toString();
    }

    private void updateCounter(String violationType, int size) {
        Integer count = counters.getOrDefault(violationType, 0) + size;
        counters.put(violationType, count);
    }

    private String getHeaderColor(String violationType) {
        switch (violationType) {
            case "MUST":
                return ANSI_RED;
            case "SHOULD":
                return ANSI_YELLOW;
            case "COULD":
                return ANSI_GREEN;
            case "HINT":
                return ANSI_CYAN;
            default:
                return ANSI_CYAN;
        }
    }
}
