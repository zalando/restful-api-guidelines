package de.zalando.zally.cli;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Reader;

public class JsonReader implements SpecsReader {
    private final Reader reader;

    public JsonReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public JSONObject read() throws CliException {
        try {
            return new JSONObject(new JSONTokener(reader));
        } catch (JSONException exception) {
            throw new CliException(CliExceptionType.CLI, "Cannot read JSON file", exception.getMessage());
        }
    }
}
