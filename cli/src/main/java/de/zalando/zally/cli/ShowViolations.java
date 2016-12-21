package de.zalando.zally.cli;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Display violations in a user friendly manner.
 */
public class ShowViolations {
    private static final String VIOLATIONS = "violations";
    private final Writer writer;

    public ShowViolations(OutputStream outputStream) {
        writer = new OutputStreamWriter(outputStream);
    }

    public int show(JsonObject violationsWrapper) throws IOException {

        // Check if violations wrapper have a valid violations list
        if (violationsWrapper == null ||
                violationsWrapper.get(VIOLATIONS) == null ||
                !violationsWrapper.get(VIOLATIONS).isArray()) {
            return 1;
        }

        JsonArray violations = violationsWrapper.get(VIOLATIONS).asArray();
        if (violations.size() == 0) {
            writer.write("No violations");
        } else {
            writer.write("You have the following violations\n");
            writer.write(violations.toString(WriterConfig.PRETTY_PRINT));
        }
        writer.flush();
        return violations.size();
    }
}
