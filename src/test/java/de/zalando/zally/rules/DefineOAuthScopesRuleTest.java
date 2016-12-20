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
    private static final String API_WITHOUT_SCOPE = "api_without_scopes_defined";
    private static final int API_WITHOUT_SCOPE_VIOLATION_COUNT = 4;
    private static final String API_WITH_DEFAULT_SCOPE = "api_with_default_scope";
    private static final int API_WITH_DEFAULT_SCOPE_VIOLATION_COUNT = 0;
    private static final String API_WITH_DEFINED_SCOPE = "api_with_defined_scope";
    private static final int API_WITH_DEFINED_SCOPE_VIOLATION_COUNT = 0;
    private static final String API_WITH_UNDEFINED_SCOPE = "api_with_undefined_scope";
    private static final int API_WITH_UNDEFINED_SCOPE_VIOLATION_COUNT = 4;
    private static final String API_WITH_DEFINED_AND_UNDEFINED_SCOPE = "api_with_defined_and_undefined_scope";
    private static final int API_WITH_DEFINED_AND_UNDEFINED_SCOPE_VIOLATION_COUNT = 2;

    @Test
    public void emptyAPI() {
        assertThat(new DefineOAuthScopesRule().validate(null)).isEmpty();
    }

    @Test
    public void apiWithoutScope() throws IOException {
        Swagger swagger = makeSwagger(API_WITHOUT_SCOPE);
        assertThat(new DefineOAuthScopesRule().validate(swagger)
                .size()).isEqualTo(API_WITHOUT_SCOPE_VIOLATION_COUNT);
    }

    @Test
    public void apiWithDefultScope() throws IOException {
        Swagger swagger = makeSwagger(API_WITH_DEFAULT_SCOPE);
        assertThat(new DefineOAuthScopesRule().validate(swagger)
                .size()).isEqualTo(API_WITH_DEFAULT_SCOPE_VIOLATION_COUNT);
    }

    @Test
    public void apiWithDefinedScope() throws IOException {
        Swagger swagger = makeSwagger(API_WITH_DEFINED_SCOPE);
        assertThat(new DefineOAuthScopesRule().validate(swagger)
                .size()).isEqualTo(API_WITH_DEFINED_SCOPE_VIOLATION_COUNT);
    }

    @Test
    public void apiWithUndefinedScope() throws IOException {
        Swagger swagger = makeSwagger(API_WITH_UNDEFINED_SCOPE);
        assertThat(new DefineOAuthScopesRule().validate(swagger)
                .size()).isEqualTo(API_WITH_UNDEFINED_SCOPE_VIOLATION_COUNT);
    }

    @Test
    public void apiWithDefinedAndUndefinedScope() throws IOException {
        Swagger swagger = makeSwagger(API_WITH_DEFINED_AND_UNDEFINED_SCOPE);
        assertThat(new DefineOAuthScopesRule().validate(swagger)
                .size()).isEqualTo(API_WITH_DEFINED_AND_UNDEFINED_SCOPE_VIOLATION_COUNT);
    }

    private Swagger makeSwagger(String api) throws IOException {
        File file = ResourceUtils.getFile("src/test/resources/" + api + ".yaml");
        return new SwaggerParser().parse(new String(Files.readAllBytes(file.toPath())));
    }
}
