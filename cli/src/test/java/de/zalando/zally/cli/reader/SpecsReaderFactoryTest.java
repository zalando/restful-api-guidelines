package de.zalando.zally.cli.reader;

import static org.junit.Assert.assertTrue;

import java.io.File;
import org.junit.Test;


public class SpecsReaderFactoryTest {
    private final String prefix = "specs_reader_factory_test";
    private final SpecsReaderFactory factory = new SpecsReaderFactory();

    @Test
    public void createsYamlReaderWhenExtensionIsYml() throws Exception {
        assertClassForGivenExtension(YamlReader.class, ".yaml");
    }

    @Test
    public void createsYamlReaderWhenExtensionIsYaml() throws Exception {
        assertClassForGivenExtension(YamlReader.class, ".yaml");
    }

    @Test
    public void createsJsonReaderIfJsonFileIsGiven() throws Exception {
        assertClassForGivenExtension(JsonReader.class, ".json");
    }

    @Test
    public void createsJsonReaderWhenExtensionIsWrong() throws Exception {
        assertClassForGivenExtension(JsonReader.class, ".abc");
        assertClassForGivenExtension(JsonReader.class, ".php");
        assertClassForGivenExtension(JsonReader.class, "");
    }

    private void assertClassForGivenExtension(Class readerClass, String extension) throws Exception {
        File tempFile = File.createTempFile(prefix, extension);

        SpecsReader reader = factory.create(tempFile.getAbsolutePath());

        assertTrue(readerClass.isInstance(reader));
    }
}
