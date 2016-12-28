package de.zalando.zally.cli;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class SpecsReaderFactory {
    public SpecsReader create(String path) throws RuntimeException {
        SpecsReader specsReader;
        if (path.endsWith(".yaml") || path.endsWith(".yml")) {
            specsReader = new YamlReader(getReader(path));
        } else {
            specsReader = new JsonReader(getReader(path));
        }
        return specsReader;
    }

    private Reader getReader(String path) throws RuntimeException {
        try {
            return new FileReader(path);
        } catch (FileNotFoundException e) {
            String message = "File " + path+ " is not found";
            throw new RuntimeException(message);
        }
    }
}
