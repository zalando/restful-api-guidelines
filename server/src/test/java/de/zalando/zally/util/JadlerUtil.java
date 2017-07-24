package de.zalando.zally.util;

import java.io.IOException;

import static de.zalando.zally.util.ResourceUtil.resourceToString;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

public class JadlerUtil {

    public static String stubResource(String resourceName) throws IOException {
        return stubResource(resourceName, resourceToString(resourceName), APPLICATION_JSON_VALUE);
    }

    public static String stubResource(String resourceName, String responseBody) {
        return stubResource(resourceName, responseBody, APPLICATION_JSON_VALUE);
    }

    public static String stubResource(String resourceName, String responseBody, String contentType) {
        String url = String.format("http://localhost:%d/%s", port(), resourceName);

        onRequest()
            .havingMethodEqualTo(GET.name())
            .havingPathEqualTo("/" + resourceName)
            .respond()
            .withStatus(OK.value())
            .withHeader(CONTENT_TYPE, contentType)
            .withBody(responseBody);

        return url;
    }

    public static String stubNotFound() {
        String remotePath = "/abcde.yaml";
        String url = "http://localhost:" + port() + remotePath;

        onRequest()
            .havingMethodEqualTo(GET.name())
            .havingPathEqualTo(remotePath)
            .respond()
            .withStatus(NOT_FOUND.value())
            .withHeader(CONTENT_TYPE, TEXT_PLAIN_VALUE)
            .withBody("NotFound");

        return url;
    }
}
