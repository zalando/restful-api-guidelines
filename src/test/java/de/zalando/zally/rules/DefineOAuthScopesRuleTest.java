package de.zalando.zally.rules;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

public class DefineOAuthScopesRuleTest {

    @Test
    public void emptyAPI() {
        assertThat(new DefineOAuthScopesRule().validate(null)).isEmpty();
    }

    @Test
    public void apiWithoutScope() throws IOException {
        int expected_violation_count = 4;
        Swagger swagger = makeSwagger("api_without_scopes_defined");
        assertThat(new DefineOAuthScopesRule().validate(swagger).size()).isEqualTo(expected_violation_count);
    }


    @Test
    public void apiWithDefinedScope() throws IOException {
        int expected_violation_count = 0;
        Swagger swagger = makeSwagger("api_with_defined_scope");
        assertThat(new DefineOAuthScopesRule().validate(swagger)
                .size()).isEqualTo(expected_violation_count);
    }

    @Test
    public void apiWithUndefinedScope() throws IOException {
        int expected_violation_count = 4;
        Swagger swagger = makeSwagger("api_with_undefined_scope");
        assertThat(new DefineOAuthScopesRule().validate(swagger)
                .size()).isEqualTo(expected_violation_count);
    }

    @Test
    public void apiWithDefinedAndUndefinedScope() throws IOException {
        int expected_violation_count = 2;
        Swagger swagger = makeSwagger("api_with_defined_and_undefined_scope");
        assertThat(new DefineOAuthScopesRule().validate(swagger)
                .size()).isEqualTo(expected_violation_count);
    }

    private Swagger makeSwagger(String api) throws IOException {
        File file = ResourceUtils.getFile("src/test/resources/" + api + ".yaml");
        return new SwaggerParser().parse(new String(Files.readAllBytes(file.toPath())));
    }
}
