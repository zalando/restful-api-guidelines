package de.zalando.zally.cli;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

import java.io.IOException;
import java.io.Reader;

public class JsonReader implements SpecsReader {
    private final Reader reader;

    public JsonReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public JsonValue read() throws CliException {
        try {
            return Json.parse(reader);
        } catch (IOException exception) {
            throw new CliException(CliExceptionType.CLI, "Cannot read JSON file", exception.getMessage());
        }
    }
}
