package de.zalando.zally.cli.reader;

import de.zalando.zally.cli.exception.CliException;
import de.zalando.zally.cli.exception.CliExceptionType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;


public class SpecsReaderFactory {
    public SpecsReader create(String path) throws CliException {
        final Reader reader = getFileReader(path);

        String[] pathComponents = path.split("\\?", 2);

        SpecsReader specsReader;
        if (pathComponents[0].endsWith(".yaml") || pathComponents[0].endsWith(".yml")) {
            specsReader = new YamlReader(reader);
        } else {
            specsReader = new JsonReader(reader);
        }

        return specsReader;
    }

    private Reader getFileReader(String path) throws CliException {
        try {
            return new FileReader(path);
        } catch (FileNotFoundException exception) {
            String message = "File " + path + " is not found";
            throw new CliException(CliExceptionType.CLI, message);
        }
    }
}
