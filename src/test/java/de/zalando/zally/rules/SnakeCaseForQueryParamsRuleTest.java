package de.zalando.zally.rules;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.apache.commons.io.IOUtils;
import org.junit.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for snake case for query params
 */
public class SnakeCaseForQueryParamsRuleTest {

    private Swagger validSwagger = getFixture("fixtures/snakeCaseForQueryParamsValid.json");
    private Swagger invalidSwaggerWithLocalParam = getFixture("fixtures/snakeCaseForQueryParamsInvalidLocalParam.json");
    private Swagger invalidSwaggerWIthInternalRef = getFixture("fixtures/snakeCaseForQueryParamsInvalidInternalRef.json");
    //private Swagger invalidSwaggerWithExternalRef = getFixture("fixtures/snakeCaseForQueryParamsInvalidExternalRef.json");

    @Test
    public void shouldFindNoViolations() {
        assertThat(new SnakeCaseForQueryParamsRule().validate(validSwagger)).isEmpty();
    }

    @Test
    public void shouldFindViolationsInLocalRef () {
        assertThat(new SnakeCaseForQueryParamsRule().validate(invalidSwaggerWithLocalParam)).isNotEmpty();
    }

    @Test
    public void shouldFindViolationsInInternalRef () {
        assertThat(new SnakeCaseForQueryParamsRule().validate(invalidSwaggerWIthInternalRef)).isNotEmpty();
    }

    /*@Test
    public void shouldFindViolationsInExternalRef () {
        assertThat(new SnakeCaseForQueryParamsRule().validate(invalidSwaggerWithExternalRef)).isNotEmpty();
    }*/

    private Swagger getFixture(String fixture) {
        SwaggerParser parser = new SwaggerParser();
        return parser.parse(readFixtureFile(fixture));
    }

    private String readFixtureFile(String fixtureFile) {
        String result = "";
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            InputStream is = classloader.getResourceAsStream(fixtureFile);
            result = IOUtils.toString(is, "UTF-8");
        }
        catch (IOException ex) {
            //TODO: Handle exception
        }
        return result;
    }
}
