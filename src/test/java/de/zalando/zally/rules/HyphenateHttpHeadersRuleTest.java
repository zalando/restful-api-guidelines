package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static de.zalando.zally.rules.HyphenateHttpHeadersRule.*;
import static junit.framework.TestCase.assertFalse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class HyphenateHttpHeadersRuleTest {
    @Test
    public void simplePoisitiveCase() {
        Swagger swagger = new Swagger();
        HashMap<String, Parameter> parameters = new HashMap<>();
        HeaderParameter parameter = new HeaderParameter();
        parameter.setName("Right-Name");
        parameters.put(parameter.getName(), parameter);
        swagger.setParameters(parameters);
        assertThat(new HyphenateHttpHeadersRule().validate(swagger)).isEmpty();
    }

    @Test
    public void simpleNegativeCase() {
        Swagger swagger = new Swagger();
        HashMap<String, Parameter> parameters = new HashMap<>();
        HeaderParameter parameter = new HeaderParameter();
        parameter.setName("CamelCaseName");
        parameters.put(parameter.getName(), parameter);
        swagger.setParameters(parameters);
        List<Violation> result = new HyphenateHttpHeadersRule().validate(swagger);
        assertThat(result).hasSameElementsAs(Collections.singletonList(
                new Violation(RULE_NAME, "Header name 'CamelCaseName' is not hyphenated", ViolationType.MUST, RULE_URL))
        );
    }

    @Test
    public void mustAcceptETag() {
        Swagger swagger = new Swagger();
        HashMap<String, Parameter> parameters = new HashMap<>();
        HeaderParameter parameter = new HeaderParameter();
        parameter.setName("ETag");
        parameters.put(parameter.getName(), parameter);
        swagger.setParameters(parameters);
        assertThat(new HyphenateHttpHeadersRule().validate(swagger)).isEmpty();
    }

    @Test
    public void emptySwaggerShouldPass() {
        Swagger swagger = new Swagger();
        swagger.setParameters(new HashMap<>());
        assertThat(new HyphenateHttpHeadersRule().validate(swagger)).isEmpty();
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
}
