package de.zalando.zally.rules;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.zalando.zally.rules.PluralizeResourceNamesRule.isPlural;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    public void negativeCaseTinbox() {
        Swagger swagger = getFixture("api_tinbox.yaml");
        List<String> results = new PluralizeResourceNamesRule()
                .validate(swagger)
                .stream()
                .map(v -> v.getPath().get())
                .collect(Collectors.toList());
        assertThat(results).hasSameElementsAs(Arrays.asList(
                "/queue/configs/{config-id}", "/queue/models", "/queue/models/{model-id}", "/queue/summaries"));
    }

    @Test
    public void checkIsPluralized() {
        assertFalse(isPlural("cat"));
        assertTrue(isPlural("dogs"));
        assertFalse(isPlural("resource"));
        assertTrue(isPlural("resources"));
        assertFalse(isPlural("payment"));
        assertTrue(isPlural("payments"));
        assertFalse(isPlural("order"));
        assertTrue(isPlural("orders"));
        assertFalse(isPlural("parcel"));
        assertTrue(isPlural("parcels"));
        assertFalse(isPlural("item"));
        assertFalse(isPlural("commission"));
        assertTrue(isPlural("commissions"));
        assertFalse(isPlural("commission_group"));
        assertTrue(isPlural("commission_groups"));
        assertFalse(isPlural("article"));
        assertTrue(isPlural("articles"));
        assertFalse(isPlural("merchant"));
        assertTrue(isPlural("merchants"));
        assertFalse(isPlural("warehouse-location"));
        assertTrue(isPlural("warehouse-locations"));
        assertFalse(isPlural("sales-channel"));
        assertTrue(isPlural("sales-channels"));
        assertFalse(isPlural("domain"));
        assertTrue(isPlural("domains"));
        assertFalse(isPlural("address"));
        assertTrue(isPlural("addresses"));
        assertFalse(isPlural("bank-account"));
        assertTrue(isPlural("bank-accounts"));

        assertTrue(isPlural("vat")); //whitelisted
    }

    private Swagger getFixture(String fixture) {
        return new SwaggerParser().read(fixture);
    }
}
