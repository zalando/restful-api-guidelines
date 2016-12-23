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

    private static final String VIOLATIONS = "violations";

    private final Writer writer;
    private final Map<String, Integer> counters = new HashMap<>();

    public ResultPrinter(OutputStream outputStream) {
        writer = new OutputStreamWriter(outputStream);
    }

    public void printViolations(List<JsonObject> violations, String violationType) throws IOException {
        String header = String.format("Found the following %s violations", violationType.toUpperCase());
        String headerUnderline = new String(new char[header.length()]).replace("\0", "=");

        if (!violations.isEmpty()) {
            writer.write(header + "\n");
            writer.write(headerUnderline + "\n");

            for (JsonObject violation : violations) {
                writer.write(formatViolation(violation) + "\n");
            }
            writer.flush();
        }

        this.updateCounter(violationType, violations.size());
    }

    public void printSummary() throws IOException {
        writer.write("\nSummary:\n=======\n");
        for (Map.Entry<String, Integer> entry : counters.entrySet()) {
            writer.write(entry.getKey().toUpperCase() + " violations: " + entry.getValue().toString() + "\n");
        }
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
