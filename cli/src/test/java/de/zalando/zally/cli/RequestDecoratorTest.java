package de.zalando.zally.cli;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class RequestDecoratorTest {

    public final static String fixture = "{\"a\":1,\"b\":\"test\"}";
    public final static String expectedResult = "{\"api_definition\":" + fixture + "}";

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