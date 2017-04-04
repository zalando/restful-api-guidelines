package de.zalando.zally.cli.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UrlWrapperStrategyTest {
    @Test
    public void shouldWrapUrl() {
        final String url = "https://example.com/test.yaml";
        final UrlWrapperStrategy wrapper = new UrlWrapperStrategy(url);

        final String result = wrapper.wrap();

        assertEquals("{\"api_definition_url\":\"https://example.com/test.yaml\"}", result);
    }
}