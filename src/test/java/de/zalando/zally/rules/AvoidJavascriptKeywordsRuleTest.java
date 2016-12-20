package de.zalando.zally.rules;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class AvoidJavascriptKeywordsRuleTest {
    private Swagger validSwagger = getFixture("fixtures/avoidJavascriptValid.json");
    private Swagger invalidSwagger = getFixture("fixtures/avoidJavascriptInvalid.json");

    @Test
    public void positiveCase() {
        assertThat(new AvoidJavascriptKeywordsRule().validate(validSwagger)).isEmpty();
    }

    @Test
    public void negativeCase () {
        List<String> results = new AvoidJavascriptKeywordsRule()
                .validate(invalidSwagger)
                .stream()
                .map(v -> v.getPath().get())
                .collect(Collectors.toList());
        assertThat(results).hasSameElementsAs(Arrays.asList("Pet.return", "Pet.typeof"));
    }

    private Swagger getFixture(String fixture) {
        return new SwaggerParser().read(fixture);
    }
}
