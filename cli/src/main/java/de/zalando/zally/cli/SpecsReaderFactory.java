package de.zalando.zally.cli;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class SpecsReaderFactory {
    public SpecsReader create(String path) throws CliException {
        SpecsReader specsReader;
        Reader reader;

        if (path.startsWith("http://") || path.startsWith("https://")) {
            reader = getHttpReader(path);
        } else {
            reader = getFileReader(path);
        }

        String[] pathComponents = path.split("\\?", 2);

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

    private Reader getHttpReader(String url) throws CliException {
        InputStream body;
        try {
            body = Unirest.get(url).asBinary().getBody();
        } catch (UnirestException exception) {
            String message = "URL " + url + " cannot be fetched";
            throw new CliException(CliExceptionType.CLI, message);
        }

        return new BufferedReader(new InputStreamReader(body));
    }
}
