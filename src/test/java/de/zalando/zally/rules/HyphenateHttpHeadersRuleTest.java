package de.zalando.zally.rules;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.util.DeserializationUtils;
import io.swagger.util.Json;
import org.junit.Test;

import java.io.InputStream;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class HyphenateHttpHeadersRuleTest {
    @Test
    public void emptySwaggerShouldPass() {
        assertThat(new HyphenateHttpHeadersRule().validate(new Swagger())).isEmpty();
    }

    @Test
    public void simpleNegativeCase() {
        Swagger swagger = new Swagger();
        HashMap<String, Parameter> parameters = new HashMap<>();
        HeaderParameter parameter = new HeaderParameter();
        parameters.put("foo", parameter);
        swagger.setParameters(parameters);
        List<Violation> result = new HyphenateHttpHeadersRule().validate(swagger);
        assertThat(result).isEqualTo(Collections.singletonList(
                new Violation(
                        "Must: Use Hyphenated HTTP Headers",
                        "Parameter name 'foo' is not hyphenated",
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
}
