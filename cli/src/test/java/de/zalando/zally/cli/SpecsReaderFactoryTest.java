package de.zalando.zally.cli;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class SpecsReaderFactoryTest {
    private final String PREFIX = "specs_reader_factory_test";
    private final SpecsReaderFactory factory = new SpecsReaderFactory();


    @Test
    public void createsYamlReader() throws Exception {
        assertClassForGivenExtension(YamlReader.class, ".yaml");
        assertClassForGivenExtension(YamlReader.class, ".yml");
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
        File tempFile = File.createTempFile(PREFIX, extension);

        SpecsReader reader = factory.create(tempFile.getAbsolutePath());

        assertTrue(readerClass.isInstance(reader));
    }
}