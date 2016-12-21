package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

/**
 * Display violations in a user friendly manner.
 */
public class ViolationsPrinter {

    private static final String VIOLATIONS = "violations";

    private final Writer writer;

    public ViolationsPrinter(OutputStream outputStream) {
        writer = new OutputStreamWriter(outputStream);
    }

    public void print(List<JsonObject> violations, String violationType) throws IOException {
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
    }

    public static String formatViolation(JsonObject violation) {
        return violation.get("title").asString()
                + ":\n\t" + violation.get("description").asString()
                + (!violation.get("path").isNull() ? ("\n\t" + violation.get("path").asString()) : "");
    }
}
