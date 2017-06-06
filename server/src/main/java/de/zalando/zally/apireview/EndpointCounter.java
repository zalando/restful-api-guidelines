package de.zalando.zally.apireview;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

public final class EndpointCounter {

    private EndpointCounter() {
    }

    public static int count(String apiDefinition) {
        try {
            Swagger swagger = new SwaggerParser().parse(apiDefinition);
            return swagger != null ? swagger.getPaths().size() : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
