package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.Swagger;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;

import java.util.List;

import static de.zalando.zally.rules.CommonFieldNamesRule.checkCommonFields;
import static org.assertj.core.api.Assertions.assertThat;

public class CommonFieldNamesRuleTest {
    private final CommonFieldNamesRule rule = new CommonFieldNamesRule();

    @Test
    public void CheckCommonFieldsEmpty() {
        assertThat(checkCommonFields("", new StringProperty())).isTrue();
    }

    @Test
    public void CheckCommonFieldsNotCommon() {
        assertThat(checkCommonFields("unknown", new StringProperty())).isTrue();
    }

    @Test
    public void CheckCommonFields() {
        assertThat(checkCommonFields("id", new StringProperty())).isTrue();
        assertThat(checkCommonFields("id", new StringProperty("UUID"))).isTrue();
        assertThat(checkCommonFields("created", new StringProperty("date-time"))).isTrue();
        assertThat(checkCommonFields("modified", new StringProperty("date-time"))).isTrue();
        assertThat(checkCommonFields("type", new StringProperty())).isTrue();
    }

    @Test
    public void CheckCommonFieldsInvalid() {
        assertThat(checkCommonFields("id", new IntegerProperty())).isFalse();
    }

    @Test
    public void validateEmpty() {
        Swagger swagger = new Swagger();
        assertThat(rule.validate(swagger)).isEmpty();
    }

    @Test
    public void validateNormal() {
        assertThat(rule.validate(getNormalSwagger())).isEmpty();
    }


    @Test
    public void validateAbnormal() {
        List<Violation> violations = rule.validate(getInvalidSwagger());
        System.out.print(violations.toString());
        assertThat(violations.size()).isEqualTo(4);
    }

    private Swagger getInvalidSwagger() {
        return new SwaggerParser().read("/common_fields_invalid.yaml");
    }

    private Swagger getNormalSwagger() {
        return new SwaggerParser().read("/common_fields.yaml");
    }


}
