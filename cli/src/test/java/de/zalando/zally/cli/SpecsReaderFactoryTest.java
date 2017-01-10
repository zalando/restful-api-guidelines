package de.zalando.zally.cli;

import com.eclipsesource.json.JsonObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static net.jadler.Jadler.*;
import static org.junit.Assert.*;

public class SpecsReaderFactoryTest {
    private final String PREFIX = "specs_reader_factory_test";
    private final SpecsReaderFactory factory = new SpecsReaderFactory();

    @Before
    public void setUp() {
        initJadler();
    }

    @After
    public void tearDown() {
        closeJadler();
    }

    @Test
    public void createsYamlReaderWhenExtensionIsYml() throws Exception {
        assertClassForGivenExtension(YamlReader.class, ".yaml");
        assertClassForGivenRemotePath(YamlReader.class, "/test.yml?token=abcde");
    }

    @Test
    public void createsYamlReaderWhenExtensionIsYaml() throws Exception {
        assertClassForGivenExtension(YamlReader.class, ".yaml");
        assertClassForGivenRemotePath(YamlReader.class, "/test.yaml");
    }

    @Test
    public void createsJsonReaderIfJsonFileIsGiven() throws Exception {
        assertClassForGivenExtension(JsonReader.class, ".json");
        assertClassForGivenRemotePath(JsonReader.class, "/test.json?token=abcde");
    }

    @Test
    public void createsJsonReaderWhenExtensionIsWrong() throws Exception {
        assertClassForGivenExtension(JsonReader.class, ".abc");
        assertClassForGivenExtension(JsonReader.class, ".php");
        assertClassForGivenExtension(JsonReader.class, "");
        assertClassForGivenRemotePath(JsonReader.class, "/test.php");
    }

    private void assertClassForGivenExtension(Class readerClass, String extension) throws Exception {
        File tempFile = File.createTempFile(PREFIX, extension);

        SpecsReader reader = factory.create(tempFile.getAbsolutePath());

        assertTrue(readerClass.isInstance(reader));
    }

    private void assertClassForGivenRemotePath(Class readerClass, String remotePath) throws Exception {
        final String responseBody = "Some fake content";

        onRequest()
                .havingMethodEqualTo("GET")
                .havingPathEqualTo(remotePath)
                .respond()
                .withStatus(200)
                .withBody(responseBody);

        String url = "http://localhost:" + port() + remotePath;
        SpecsReader reader = factory.create(url);
        assertTrue(readerClass.isInstance(reader));
    }
}
