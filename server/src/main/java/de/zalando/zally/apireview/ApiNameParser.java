package de.zalando.zally.apireview;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

public class ApiNameParser {

    private ApiNameParser() {}

    public static String extractApiName(String apiDefinition) {
        try {
            Swagger swagger = new SwaggerParser().parse(apiDefinition);
            if (swagger == null || swagger.getInfo() == null) {
                return null;
            }
            return swagger.getInfo().getTitle().trim();
        } catch (Exception e) {
            return null;
        }
    }
}
