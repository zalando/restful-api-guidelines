package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.ModelImpl;
import io.swagger.models.Swagger;
import io.swagger.models.properties.StringProperty;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SnakeCaseInPropNameRuleTest extends SnakeCaseInPropNameRule {

    @Test
    public void isSnakeCaseForEmpty() {
        assertThat(isSnakeCase("")).isTrue();
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
        assertThat(isSnakeCase("_")).isTrue();
    }

    @Test
    public void isSnakeCaseForAllLowerCaseAndHyphen() {
        assertThat(isSnakeCase("customer-number")).isFalse();
    }

    @Test
    public void isSnakeCaseForUpperCaseAndUnderscore() {
        assertThat(isSnakeCase("CUSTOMER_NUMBER")).isFalse();
    }


    @Test
    public void validateEmptyPath(){
        Swagger swagger = new Swagger();
        assertThat(validate(swagger)).isEmpty();
    }

    @Test
    public void validateNormalProperty(){
        Swagger swagger = new Swagger();
        StringProperty property = new StringProperty();
        property.setName("test_property");
        ModelImpl model = new ModelImpl();
        model.addProperty("test_property", property );
        swagger.addDefinition("ExampleDefintion", model);
        assertThat(validate(swagger)).isEmpty();
    }

    @Test
    public void validateMultipleNormalProperties(){
        Swagger swagger = new Swagger();
        StringProperty property = new StringProperty();
        property.setName("test_property");
        StringProperty property2 = new StringProperty();
        property2.setName("test_property_two");
        ModelImpl model = new ModelImpl();
        model.addProperty("test_property", property );
        model.addProperty("test_property_two", property2 );
        swagger.addDefinition("ExampleDefintion", model);
        assertThat(validate(swagger)).isEmpty();
    }

    @Test
    public void validateMultipleNormalPropertiesInMultipleDefinitions(){
        Swagger swagger = new Swagger();
        StringProperty property = new StringProperty();
        property.setName("test_property");
        StringProperty property2 = new StringProperty();
        property2.setName("test_property_two");
        ModelImpl model = new ModelImpl();
        model.addProperty("test_property", property );
        ModelImpl model2 = new ModelImpl();
        model2.addProperty("test_property_two", property2 );
        swagger.addDefinition("ExampleDefintion", model);
        swagger.addDefinition("ExampleDefintionTwo", model2);
        assertThat(validate(swagger)).isEmpty();
    }

    @Test
    public void validateFalseProperty(){
        Swagger swagger = new Swagger();
        StringProperty property = new StringProperty();
        property.setName("TEST_PROPERTY");
        ModelImpl model = new ModelImpl();
        model.setTitle("ExampleDefintion");
        model.addProperty("TEST_PROPERTY", property );
        swagger.addDefinition("ExampleDefintion", model);
        List<Violation> result = validate(swagger);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualToComparingFieldByField(new Violation(title, description + "\n Violating property: " + property.getName() + " in definition: " + model.getTitle(), ViolationType.MUST, ruleLink));
    }

    @Test
    public void validateMultipleFalsePropertiesInMultipleDefinitions(){
        Swagger swagger = new Swagger();
        StringProperty property = new StringProperty();
        property.setName("TEST_PROPERTY");
        StringProperty property2 = new StringProperty();
        property2.setName("test_property_TWO");
        ModelImpl model = new ModelImpl();
        model.setTitle("ExampleDefintion");
        model.addProperty("TEST_PROPERTY", property );
        ModelImpl model2 = new ModelImpl();
        model2.setTitle("ExampleDefintionTwo");
        model2.addProperty("test_property_two", property2 );
        swagger.addDefinition("ExampleDefintion", model);
        swagger.addDefinition("ExampleDefintionTwo", model2);
        List<Violation> result = validate(swagger);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(new Violation(title, description + "\n Violating property: " + property.getName() + " in definition: " + model.getTitle(), ViolationType.MUST, ruleLink));
        assertThat(result.get(1)).isEqualToComparingFieldByField(new Violation(title, description + "\n Violating property: " + property2.getName() + " in definition: " + model2.getTitle(), ViolationType.MUST, ruleLink));
    }
}
