package de.zalando.zally.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class JsonReaderTest {
    private static final String fixture = "{\"swagger\": \"2.0\",\"info\": {\"title\": \"Product Service\"}}";
    private static final String brokenFixture = "{\"swagger\": \"2.0\",";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void returnJsonValueWhenEverythingIsOk() {
        JsonReader reader = new JsonReader(getFixtureReader(fixture));
        JSONObject value = reader.read();

        assertEquals("2.0", value.get("swagger"));
        assertEquals("Product Service", value.getJSONObject("info").get("title"));
    }

    @Test
    public void raisesCliExceptionWhenInputIsBroken() {
        expectedException.expect(CliException.class);
        expectedException.expectMessage("A JSONObject text must end with '}' at 19 [character 20 line 1]");

        JsonReader reader = new JsonReader(getFixtureReader(brokenFixture));

        assertNotNull(reader);

        reader.read();
    }

    private InputStreamReader getFixtureReader(String fixture) {
        InputStream inputStream = new ByteArrayInputStream(fixture.getBytes());
        return new InputStreamReader(inputStream);
    }
}