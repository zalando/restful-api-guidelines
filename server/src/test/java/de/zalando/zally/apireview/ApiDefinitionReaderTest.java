package de.zalando.zally.apireview;

import de.zalando.zally.exception.MissingApiDefinitionException;
import de.zalando.zally.violation.ApiDefinitionRequest;
import net.jadler.stubbing.server.jdk.JdkStubHttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadlerUsing;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;
import static org.junit.Assert.assertEquals;

public class ApiDefinitionReaderTest {

    private final String contentInJson = "{\"swagger\":\"2.0\"}";

    private ApiDefinitionReader reader;

    @Before
    public void setUp() {
        initJadlerUsing(new JdkStubHttpServer());
        reader = new ApiDefinitionReader(new RestTemplate());
    }

    @After
    public void tearDown() {
        closeJadler();
    }

    @Test(expected = MissingApiDefinitionException.class)
    public void shouldThrowMissingApiDefinitionExceptionWhenDefinitionIsNotFound() {
        reader.read(new ApiDefinitionRequest());
    }

    @Test
    public void shouldReturnStringWhenApiDefinitionIsFound() {
        final String result = reader.read(getJsonNodeWithApiDefinition());
        assertEquals("{\"swagger\":\"2.0\"}", result);
    }

    @Test
    public void shouldReadJsonSwaggerDefinitionFromUrl() {
        final String fileName = "test.json";
        final String contentType = "application/json";
        final String url = startServer(fileName, contentInJson, contentType);

        final String result = reader.read(getJsonNodeWithApiDefinitionUrl(url));

        assertEquals(contentInJson, result);
    }

    @Test
    public void shouldReadYamlSwaggerDefinitionFromUrl() {
        final String fileName = "test.yaml";
        final String content = "swagger: \"2.0\"";
        final String contentType = "application/x-yaml";
        final String url = startServer(fileName, content, contentType);

        final String result = reader.read(getJsonNodeWithApiDefinitionUrl(url));

        assertEquals(content, result);
    }

    @Test
    public void shouldRetryLoadingOfUrlIfEndsWithSpecialEncodedCharacters() {
        final String result = reader.read(getJsonNodeWithApiDefinitionUrlWithSpecialCharacters());
        assertEquals(contentInJson, result);
    }

    private ApiDefinitionRequest getJsonNodeWithApiDefinition() {
        ApiDefinitionRequest request = new ApiDefinitionRequest();
        request.setApiDefinition("{\"swagger\":\"2.0\"}");
        request.setApiDefinitionUrl("http://zalando.de");
        return request;
    }

    private ApiDefinitionRequest getJsonNodeWithApiDefinitionUrlWithSpecialCharacters() {
        final String fileName = "test.json";
        final String contentType = "application/json";
        return getJsonNodeWithApiDefinitionUrl(startServer(fileName, contentInJson, contentType) + "%3D%3D");
    }

    private ApiDefinitionRequest getJsonNodeWithApiDefinitionUrl(String url) {
        ApiDefinitionRequest request = new ApiDefinitionRequest();
        request.setApiDefinitionUrl(url);
        return request;
    }

    private String startServer(final String fileName, final String content, final String contentType) {
        final String remotePath = "/" + fileName;
        final String url = "http://localhost:" + port() + remotePath;

        onRequest()
            .havingMethodEqualTo("GET")
            .havingPathEqualTo(remotePath)
            .respond()
            .withStatus(200)
            .withHeader("Content-Type", contentType)
            .withBody(content);

        return url;
    }
}
