package de.zalando.zally.cli.api;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class RequestWrapperFactoryTest {
    @Test
    public void shouldCreateUrlWrapperWhenHttpUrlIsPassed() {
        final String path = "http://example.com";
        final RequestWrapperStrategy strategy = new RequestWrapperFactory().create(path);

        assertTrue(strategy instanceof UrlWrapperStrategy);
    }

    @Test
    public void shouldCreateUrlWrapperWhenHttpsUrlIsPassed() {
        final String path = "https://example.com";
        final RequestWrapperStrategy strategy = new RequestWrapperFactory().create(path);

        assertTrue(strategy instanceof UrlWrapperStrategy);
    }

    @Test
    public void shouldCreateSpecsWrapperWhenFilePathIsPassed() {
        final String path = this.getClass().getClassLoader().getResource("test.yaml").getFile();
        final RequestWrapperStrategy strategy = new RequestWrapperFactory().create(path);

        assertTrue(strategy instanceof SpecsWrapperStrategy);
    }
}
