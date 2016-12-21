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

import static de.zalando.zally.rules.PascalCaseHttpHeadersRule.RULE_NAME;
import static de.zalando.zally.rules.PascalCaseHttpHeadersRule.RULE_URL;
import static de.zalando.zally.rules.PascalCaseHttpHeadersRule.DESCRIPTION;
import static org.assertj.core.api.Assertions.assertThat;

public class PascalCaseHttpHeadersRuleTest {

    @Test
    public void simplePositiveCase() {
        Swagger swagger = new Swagger();
        HashMap<String, Parameter> parameters = new HashMap<>();
        HeaderParameter parameter = new HeaderParameter();
        parameter.setName("Right-Name");
        parameters.put(parameter.getName(), parameter);
        swagger.setParameters(parameters);
        assertThat(new PascalCaseHttpHeadersRule().validate(swagger)).isEmpty();
    }

    @Test
    public void simpleNegativeCase() {
        Swagger swagger = new Swagger();
        String badHeader = "kebap-case-name";
        HashMap<String, Parameter> parameters = new HashMap<>();
        HeaderParameter parameter = new HeaderParameter();
        parameter.setName(badHeader);
        parameters.put(parameter.getName(), parameter);
        swagger.setParameters(parameters);
        List<Violation> result = new PascalCaseHttpHeadersRule().validate(swagger);
        assertThat(result).hasSameElementsAs(Collections.singletonList(
                new Violation(RULE_NAME, String.format(DESCRIPTION, badHeader), ViolationType.SHOULD, RULE_URL))
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
        assertThat(new PascalCaseHttpHeadersRule().validate(swagger)).isEmpty();
    }

    @Test
    public void emptySwaggerShouldPass() {
        Swagger swagger = new Swagger();
        swagger.setParameters(new HashMap<>());
        assertThat(new PascalCaseHttpHeadersRule().validate(swagger)).isEmpty();
    }

    @Test
    public void positiveCaseSpp() {
        Swagger swagger = new SwaggerParser().read("api_spp.json");
        assertThat(new PascalCaseHttpHeadersRule().validate(swagger)).isEmpty();
    }

    @Test
    public void positiveCaseTinbox() {
        Swagger swagger = new SwaggerParser().read("api_tinbox.yaml");
        assertThat(new PascalCaseHttpHeadersRule().validate(swagger)).isEmpty();
    }
}

