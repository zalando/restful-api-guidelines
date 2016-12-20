package de.zalando.zally.rules;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.apache.commons.io.IOUtils;
import org.junit.*;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for snake case for query params
 */
public class SnakeCaseForQueryParamsTest {

    private Swagger invalidSwagger = getFixture("fixtures/snakeCaseForQueryParamsInvalid.json");
    private Swagger validSwagger = getFixture("fixtures/snakeCaseForQueryParamsValid.json");

    @Test
    public void validCase() {
        assertThat(new SnakeCaseForQueryParams().validate(validSwagger)).isEmpty();
    }

    @Test
    public void invalidCase() {
        assertThat(new SnakeCaseForQueryParams().validate(invalidSwagger)).isNotEmpty();
    }

    private Swagger getFixture(String fixture) {
        SwaggerParser parser = new SwaggerParser();
        return parser.readWithInfo(readFixtureFile(fixture)).getSwagger();
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
