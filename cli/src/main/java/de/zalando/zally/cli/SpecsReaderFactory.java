package de.zalando.zally.cli;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.*;

public class SpecsReaderFactory {
    public SpecsReader create(String path) throws RuntimeException {
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

    private Reader getFileReader(String path) throws RuntimeException {
        try {
            return new FileReader(path);
        } catch (FileNotFoundException e) {
            String message = "File " + path+ " is not found";
            throw new RuntimeException(message);
        }
    }

    private Reader getHttpReader(String url) throws RuntimeException {
        InputStream body;
        try {
            body = Unirest.get(url).asBinary().getBody();
        } catch (UnirestException e) {
            String message = "URL " + url + " cannot be fetched";
            throw new RuntimeException(message);
        }

        return new BufferedReader(new InputStreamReader(body));
    }
}
