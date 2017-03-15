package de.zalando.zally.cli.api;

import static org.junit.Assert.assertEquals;

import de.zalando.zally.cli.readers.JsonReader;
import de.zalando.zally.cli.readers.SpecsReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.junit.Test;


public class RequestDecoratorTest {

    public static final String fixture = "{\"a\":1,\"b\":\"test\"}";
    public static final String expectedResult = "{\"api_definition\":" + fixture + "}";

    @Test
    public void wrapsTheResult() {
        SpecsReader reader = getReader();

        RequestDecorator decorator = new RequestDecorator(reader);
        String result = decorator.getRequestBody();

        assertEquals(expectedResult, result);
    }

    private SpecsReader getReader() {
        InputStream inputStream = new ByteArrayInputStream(fixture.getBytes());
        return new JsonReader(new InputStreamReader(inputStream));
    }
}