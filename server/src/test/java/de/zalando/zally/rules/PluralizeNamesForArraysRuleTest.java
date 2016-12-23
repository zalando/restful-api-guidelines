package de.zalando.zally.rules;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PluralizeNamesForArraysRuleTest {
    private Swagger validSwagger = getFixture("fixtures/pluralizeArrayNamesValid.json");
    private Swagger invalidSwagger = getFixture("fixtures/pluralizeArrayNamesInvalid.json");


    @Test
    public void positiveCase() {
        assertThat(new PluralizeNamesForArraysRule().validate(validSwagger)).isEmpty();
    }

    @Test
    public void negativeCase() {
        List<String> results = new PluralizeNamesForArraysRule()
                .validate(invalidSwagger)
                .stream()
                .map(v -> v.getPath().get())
                .collect(Collectors.toList());
        assertThat(results).hasSameElementsAs(Arrays.asList("Pet.tag", "Pet.name"));
    }

    @Test
    public void positiveCaseSpp() {
        Swagger swagger = getFixture("api_spp.json");
        assertThat(new PluralizeNamesForArraysRule().validate(swagger)).isEmpty();
    }

    @Test
    public void positiveCaseTinbox() {
        Swagger swagger = getFixture("api_tinbox.yaml");
        assertThat(new PluralizeNamesForArraysRule().validate(swagger)).isEmpty();
    }

    private Swagger getFixture(String fixture) {
        try {
            File file = ResourceUtils.getFile("src/test/resources/" + fixture);
            return new SwaggerParser().parse(new String(Files.readAllBytes(file.toPath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to read fixture", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to read fixture", e);
        }
    }
}
