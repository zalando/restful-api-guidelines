package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class HyphenateHttpHeadersRuleTest {
    @Test
    public void emptySwaggerShouldPass() {
        Swagger swagger = new Swagger();
        swagger.setParameters(new HashMap<>());
        assertThat(new HyphenateHttpHeadersRule().validate(swagger)).isEmpty();
    }

    @Test
    public void simpleNegativeCase() {
        Swagger swagger = new Swagger();
        HashMap<String, Parameter> parameters = new HashMap<>();
        HeaderParameter parameter = new HeaderParameter();
        parameter.setName("foo");
        parameters.put(parameter.getName(), parameter);
        swagger.setParameters(parameters);
        List<Violation> result = new HyphenateHttpHeadersRule().validate(swagger);
        assertThat(result).hasSameElementsAs(Collections.singletonList(
                new Violation(
                        "Must: Use Hyphenated HTTP Headers",
                        "Header name 'foo' is not hyphenated",
                        ViolationType.MUST,
                        "http://zalando.github.io/restful-api-guidelines/naming/Naming.html")
                )
        );
    }

    @Test
    public void positiveCaseSpp() {
        Swagger swagger = new SwaggerParser().read("api_spp.json");
        assertThat(new HyphenateHttpHeadersRule().validate(swagger)).isEmpty();
    }

    @Test
    public void positiveCaseTinbox() {
        Swagger swagger = new SwaggerParser().read("api_tinbox.yaml");
        assertThat(new HyphenateHttpHeadersRule().validate(swagger)).isEmpty();
    }

    @Test
    public void hyphenationCheck() {
        HyphenateHttpHeadersRule rule = new HyphenateHttpHeadersRule();
        assertTrue(rule.isHyphenated(""));
        assertTrue(rule.isHyphenated("A"));
        assertTrue(rule.isHyphenated("Aa"));
        assertFalse(rule.isHyphenated("aA"));
        assertFalse(rule.isHyphenated("AA"));
        assertTrue(rule.isHyphenated("A-A"));
        assertTrue(rule.isHyphenated("This-Is-Some-Hyphenated-String"));
        assertTrue(rule.isHyphenated("this-is-other-hyphenated-string"));
        assertFalse(rule.isHyphenated("Sorry no hyphens here"));
        assertFalse(rule.isHyphenated("CamelCaseIsNotAcceptableAndShouldBeIllegal"));
    }
}
