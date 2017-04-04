package de.zalando.zally.cli.api;

import static org.junit.Assert.assertEquals;

import de.zalando.zally.cli.reader.JsonReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.junit.Test;

public class SpecsWrapperStrategyTest {
    @Test
    public void shouldReadAndWrapSpecification() {
        final SpecsWrapperStrategy wrapper = new SpecsWrapperStrategy(getJsonReader());

        final String result = wrapper.wrap();

        assertEquals("{\"api_definition\":{\"hello\":\"world\"}}", result);
    }

    private JsonReader getJsonReader() {
        final String fixture = "{\"hello\":\"world\"}";
        final InputStream inputStream = new ByteArrayInputStream(fixture.getBytes());
        return new JsonReader(new InputStreamReader(inputStream));
    }
}