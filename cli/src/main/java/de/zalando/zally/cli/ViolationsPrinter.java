package de.zalando.zally.cli;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;

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

    public void print(List<String> violations, String violationType) throws IOException {
        String header = String.format("Found the following %s violations", violationType.toUpperCase());
        String headerUnderline = new String(new char[header.length()]).replace("\0", "=");

        if (!violations.isEmpty()) {
            writer.write(header + "\n");
            writer.write(headerUnderline + "\n");

            for (String violation: violations) {
                writer.write(violation + "\n");
            }
            writer.flush();
        }
    }
}
