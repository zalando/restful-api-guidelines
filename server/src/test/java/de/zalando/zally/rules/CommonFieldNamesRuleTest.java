package de.zalando.zally.rules;

import io.swagger.models.Swagger;
import io.swagger.models.properties.AbstractProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonFieldNamesRuleTest {

    private final CommonFieldNamesRule rule = new CommonFieldNamesRule();

    @Test
    public void matchesCommonFieldsTypeEmpty() {
        assertThat(CommonFieldNamesRule.matchesCommonFieldsType("", new StringProperty())).isTrue();
    }

    @Test
    public void matchesCommonFieldsTypeNotCommon() {
        assertThat(CommonFieldNamesRule.matchesCommonFieldsType("unknown", new StringProperty())).isTrue();
    }

    @Test
    public void matchesCommonFieldsType() {
        assertThat(CommonFieldNamesRule.matchesCommonFieldsType("id", new StringProperty())).isTrue();
        assertThat(CommonFieldNamesRule.matchesCommonFieldsType("id", new StringProperty("UUID"))).isTrue();
        assertThat(CommonFieldNamesRule.matchesCommonFieldsType("created", new StringProperty("date-time"))).isTrue();
        assertThat(CommonFieldNamesRule.matchesCommonFieldsType("modified", new StringProperty("date-time"))).isTrue();
        assertThat(CommonFieldNamesRule.matchesCommonFieldsType("type", new StringProperty())).isTrue();
    }

    @Test
    public void matchingShouldBeCaseInsensitive() {
        assertThat(CommonFieldNamesRule.matchesCommonFieldsType("iD", new IntegerProperty())).isFalse();
        assertThat(CommonFieldNamesRule.matchesCommonFieldsType("CREATED", new IntegerProperty())).isFalse();
        assertThat(CommonFieldNamesRule.matchesCommonFieldsType("tYpE", new AbstractProperty() {
            @Override
            public String getType() {
                return null;
            }
        })).isFalse();

        assertThat(CommonFieldNamesRule.matchesCommonFieldsFormat("CREated", new StringProperty("time"))).isFalse();
        assertThat(CommonFieldNamesRule.matchesCommonFieldsFormat("ID", new StringProperty("uuid"))).isTrue();
        assertThat(CommonFieldNamesRule.matchesCommonFieldsFormat("Id", new StringProperty())).isTrue();
    }

    @Test
    public void matchesCommonFieldsTypeInvalid() {
        assertThat(CommonFieldNamesRule.matchesCommonFieldsType("id", new IntegerProperty())).isFalse();
    }

    @Test
    public void matchesCommonFieldsFormatEmpty() {
        assertThat(CommonFieldNamesRule.matchesCommonFieldsFormat("", new StringProperty())).isTrue();
    }

    @Test
    public void matchesCommonFieldsFormatNotCommon() {
        assertThat(CommonFieldNamesRule.matchesCommonFieldsFormat("unknown", new StringProperty())).isTrue();
    }

    @Test
    public void matchesCommonFieldsFormat() {
        assertThat(CommonFieldNamesRule.matchesCommonFieldsFormat("id", new StringProperty("UUID"))).isTrue();
        assertThat(CommonFieldNamesRule.matchesCommonFieldsFormat("created", new StringProperty("date-time"))).isTrue();
        assertThat(CommonFieldNamesRule.matchesCommonFieldsFormat("modified", new StringProperty("date-time"))).isTrue();
    }

    @Test
    public void matchesCommonFieldFormatInvalid() {
        assertThat(CommonFieldNamesRule.matchesCommonFieldsType("id", new IntegerProperty())).isFalse();
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
        assertThat(rule.validate(getInvalidSwagger()).size()).isEqualTo(5);
    }

    private Swagger getInvalidSwagger() {
        return new SwaggerParser().read("/common_fields_invalid.yaml");
    }

    private Swagger getNormalSwagger() {
        return new SwaggerParser().read("/common_fields.yaml");
    }
}
