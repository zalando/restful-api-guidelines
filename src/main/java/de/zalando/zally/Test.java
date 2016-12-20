package de.zalando.zally;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.util.Json;

/**
 * Created by chowald on 20/12/16.
 */
public class Test {
    public static void main(String[] args) {
        Swagger swagger = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");
        System.out.print(Json.pretty(swagger));
    }
}
