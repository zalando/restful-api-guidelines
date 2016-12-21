package de.zalando.zally.rules;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PluralizeResourceNamesRuleTest {
    private Swagger validSwagger = getFixture("fixtures/pluralizeResourcesValid.json");
    private Swagger invalidSwagger = getFixture("fixtures/pluralizeResourcesInvalid.json");


    @Test
    public void positiveCase() {
        assertThat(new PluralizeResourceNamesRule().validate(validSwagger)).isEmpty();
    }

    @Test
    public void negativeCase () {
        List<String> results = new PluralizeResourceNamesRule()
                .validate(invalidSwagger)
                .stream()
                .map(v -> v.getPath().get())
                .collect(Collectors.toList());
        assertThat(results).hasSameElementsAs(Arrays.asList("/pet/cats", "/pets/cats/{cat-id}/tail/{tail-id}/strands"));
    }

    @Test
    public void positiveCaseSpp() {
        Swagger swagger = getFixture("api_spp.json");
        assertThat(new PluralizeResourceNamesRule().validate(swagger)).isEmpty();
    }

    @Test
    public void positiveCaseTinbox() {
        Swagger swagger = getFixture("api_tinbox.yaml");
        assertThat(new PluralizeResourceNamesRule().validate(swagger)).isEmpty();
    }

    private Swagger getFixture(String fixture) {
        return new SwaggerParser().read(fixture);
    }
}
