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

    private static final String VIOLATIONS = "violations";

    private final Writer writer;
    private final Map<String, Integer> counters = new HashMap<>();

    public ResultPrinter(OutputStream outputStream) {
        writer = new OutputStreamWriter(outputStream);
    }

    public void printViolations(List<JsonObject> violations, String violationType) throws IOException {
        String header = String.format("Found the following %s violations", violationType.toUpperCase());

        if (!violations.isEmpty()) {
            printHeader(ANSI_YELLOW, header);

            for (JsonObject violation : violations) {
                writer.write(formatViolation(violation) + "\n");
            }
            writer.flush();
        }

        this.updateCounter(violationType, violations.size());
    }

    public void printSummary() throws IOException {
        printHeader(ANSI_GREEN, "Summary:");
        for (Map.Entry<String, Integer> entry : counters.entrySet()) {
            writer.write(entry.getKey().toUpperCase() + " violations: " + entry.getValue().toString() + "\n");
        }
        writer.flush();
    }

    public void printHeader(String ansiColor, String header) throws IOException {
        String headerUnderline = new String(new char[header.length()]).replace("\0", "=");
        writer.write(ansiColor + "\n" + header + "\n" + headerUnderline + "\n" + ANSI_RESET);
        writer.flush();
    }

    public static String formatViolation(JsonObject violation) {
        return violation.get("title").asString()
                + ":\n\t" + violation.get("description").asString()
                + (!violation.get("path").isNull() ? ("\n\t" + violation.get("path").asString()) : "");
    }

    private void updateCounter(String violationType, int size) {
        Integer count = counters.getOrDefault(violationType, 0) + size;
        counters.put(violationType, count);
    }
}
