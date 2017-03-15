package de.zalando.zally.cli.readers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.zalando.zally.cli.exceptions.CliException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class YamlReaderTest {
    private static final String fixture = "swagger: \"2.0\"\ninfo:\n  title: Partner Service Adapter";
    private static final String brokenFixture = ": \"2.0\"\ninfo:\n  title: Partner Service Adapter";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void returnJsonValueWhenEverythingIsOk() {
        YamlReader reader = new YamlReader(getFixtureReader(fixture));
        JSONObject value = reader.read();

        assertEquals("2.0", value.get("swagger"));
        assertEquals("Partner Service Adapter", value.getJSONObject("info").get("title"));
    }

    @Test
    public void raisesCliExceptionWhenInputIsBroken() {
        expectedException.expect(CliException.class);
        expectedException.expectMessage("A JSONObject text must begin with '{' at 1 [character 2 line 1]");

        JsonReader reader = new JsonReader(getFixtureReader(brokenFixture));

        assertNotNull(reader);

        reader.read();
    }

    private InputStreamReader getFixtureReader(String fixture) {
        InputStream inputStream = new ByteArrayInputStream(fixture.getBytes());
        return new InputStreamReader(inputStream);
    }
}
