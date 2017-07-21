package de.zalando.zally.util;

import de.zalando.zally.dto.ApiDefinitionRequest;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class ResourceUtil {

    private ResourceUtil() {
    }

    public static String resourceToString(String resourceName) throws IOException {
        return IOUtils.toString(ClassLoader.getSystemResourceAsStream(resourceName));
    }

    public static ApiDefinitionRequest readApiDefinition(String resourceName) throws IOException {
        ApiDefinitionRequest request = new ApiDefinitionRequest();
        String apiDefinitionJson = resourceToString(resourceName);
        request.setApiDefinition(apiDefinitionJson);
        return request;
    }
}
