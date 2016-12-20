package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.ModelImpl;
import io.swagger.models.Swagger;
import io.swagger.models.properties.StringProperty;
import org.junit.Test;

import java.util.List;

import static de.zalando.zally.rules.SnakeCaseInPropNameRule.isSnakeCase;
import static org.assertj.core.api.Assertions.assertThat;

public class SnakeCaseInPropNameRuleTest {

    SnakeCaseInPropNameRule rule = new SnakeCaseInPropNameRule();

    Swagger TEST_SWAGGER = new Swagger();
    ModelImpl TEST_DEFINITION_1 = new ModelImpl();
    ModelImpl TEST_DEFINITION_2 = new ModelImpl();
    StringProperty TEST_PORPERTY_1 = new StringProperty();
    StringProperty TEST_PORPERTY_2 = new StringProperty();

    @Test
    public void isSnakeCaseForEmpty() {
        assertThat(isSnakeCase("")).isFalse();
    }

    @Test
    public void isSnakeCaseForCorrectInput() {
        assertThat(isSnakeCase("customer_number")).isTrue();
    }

    @Test
    public void isSnakeCaseForOnlyLowerCase() {
        assertThat(isSnakeCase("customer")).isTrue();
    }

    @Test
    public void isSnakeCaseForUnderscore() {
        assertThat(isSnakeCase("_")).isFalse();
    }

    @Test
    public void isSnakeCaseForAllLowerCaseAndHyphen() {
        assertThat(isSnakeCase("customer-number")).isFalse();
    }

    @Test
    public void isSnakeCaseForStartWithUnderscore() {
        assertThat(isSnakeCase("_customer_number")).isFalse();
    }

    @Test
    public void isSnakeCaseForUpperCaseAndUnderscore() {
        assertThat(isSnakeCase("CUSTOMER_NUMBER")).isFalse();
    }


    @Test
    public void validateEmptyPath(){
        assertThat(rule.validate(TEST_SWAGGER)).isEmpty();
    }

    @Test
    public void validateNormalProperty(){
        TEST_DEFINITION_1.addProperty("test_property", TEST_PORPERTY_1 );
        TEST_SWAGGER.addDefinition("ExampleDefintion", TEST_DEFINITION_1);
        assertThat(rule.validate(TEST_SWAGGER)).isEmpty();
    }

    @Test
    public void validateMultipleNormalProperties(){
        TEST_DEFINITION_1.addProperty("test_property", TEST_PORPERTY_1 );
        TEST_DEFINITION_1.addProperty("test_property_two", TEST_PORPERTY_2 );
        TEST_SWAGGER.addDefinition("ExampleDefintion", TEST_DEFINITION_1);
        assertThat(rule.validate(TEST_SWAGGER)).isEmpty();
    }

    @Test
    public void validateMultipleNormalPropertiesInMultipleDefinitions(){
        TEST_DEFINITION_1.addProperty("test_property", TEST_PORPERTY_1 );
        TEST_DEFINITION_2.addProperty("test_property_two", TEST_PORPERTY_2 );
        TEST_SWAGGER.addDefinition("ExampleDefintion", TEST_DEFINITION_1);
        TEST_SWAGGER.addDefinition("ExampleDefintionTwo", TEST_DEFINITION_2);
        assertThat(rule.validate(TEST_SWAGGER)).isEmpty();
    }

    @Test
    public void validateFalseProperty(){
        TEST_DEFINITION_1.addProperty("TEST_PROPERTY", TEST_PORPERTY_1 );
        TEST_SWAGGER.addDefinition("ExampleDefintion", TEST_DEFINITION_1);
        List<Violation> result = rule.validate(TEST_SWAGGER);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getDescription()).contains("\n Violating property: TEST_PROPERTY in definition: ExampleDefintion");
    }

    @Test
    public void validateMultipleFalsePropertiesInMultipleDefinitions(){
        TEST_DEFINITION_1.addProperty("TEST_PROPERTY", TEST_PORPERTY_1 );
        TEST_DEFINITION_2.addProperty("test_property_TWO", TEST_PORPERTY_2);
        TEST_SWAGGER.addDefinition("ExampleDefintion", TEST_DEFINITION_1);
        TEST_SWAGGER.addDefinition("ExampleDefintionTwo", TEST_DEFINITION_2);
        List<Violation> result = rule.validate(TEST_SWAGGER);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getDescription()).contains("\n Violating property: TEST_PROPERTY in definition: ExampleDefintion");
        assertThat(result.get(1).getDescription()).contains("\n Violating property: test_property_TWO in definition: ExampleDefintionTwo");
    }
}
