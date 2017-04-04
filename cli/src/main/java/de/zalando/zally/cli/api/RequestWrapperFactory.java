package de.zalando.zally.cli.api;

import de.zalando.zally.cli.reader.SpecsReader;
import de.zalando.zally.cli.reader.SpecsReaderFactory;

public class RequestWrapperFactory {
    public RequestWrapperStrategy create(final String path) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return new UrlWrapperStrategy(path);
        } else {
            SpecsReader reader = new SpecsReaderFactory().create(path);
            return new SpecsWrapperStrategy(reader);
        }
    }
}
