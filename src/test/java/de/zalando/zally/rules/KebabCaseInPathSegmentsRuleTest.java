package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.zalando.zally.rules.KebabCaseInPathSegmentsRule.isLowerCaseAndHyphens;
import static de.zalando.zally.rules.KebabCaseInPathSegmentsRule.isPathVariable;
import static org.assertj.core.api.Assertions.assertThat;

public class KebabCaseInPathSegmentsRuleTest {

    KebabCaseInPathSegmentsRule RULE = new KebabCaseInPathSegmentsRule();
    Swagger TEST_SWAGGER = new Swagger();
    String TEST_PATH_1 = "/shipment-order/{shipment_order_id}";
    String TEST_PATH_2 = "/partner-order/{partner_order_id}";
    String TEST_PATH_3 = "/partner-order/{partner_order_id}/partner-order/{partner_order_id}";
    String WRONG_TEST_PATH_1 = "/shipment_order/{shipment_order_id}";
    String WRONG_TEST_PATH_2 = "/partner-order/{partner_order_id}/partner-order1/{partner_order_id}";


    @Test
    public void isLowerCaseAndHyphensForEmpty() {
        assertThat(isLowerCaseAndHyphens("")).isTrue();
    }

    @Test
    public void isLowerCaseAndHyphensForAllLowerCaseAndHyphen() {
        assertThat(isLowerCaseAndHyphens("shipment-orders")).isTrue();
    }

    @Test
    public void isLowerCaseAndHyphensForAllLowerCase() {
        assertThat(isLowerCaseAndHyphens("shipment")).isTrue();
    }

    @Test
    public void isLowerCaseAndHyphensForHyphen() {
        assertThat(isLowerCaseAndHyphens("-")).isTrue();
    }

    @Test
    public void isLowerCaseAndHyphensForAllUpperCaseAndHyphen() {
        assertThat(isLowerCaseAndHyphens("SHIPMENT-ORDERS")).isFalse();
    }

    @Test
    public void isLowerCaseAndHyphensForOneUpperCaseAndHyphen() {
        assertThat(isLowerCaseAndHyphens("Shipment-orders")).isFalse();
    }

    @Test
    public void isLowerCaseAndHyphensForNumbers() {
        assertThat(isLowerCaseAndHyphens("orders12")).isFalse();
    }
    @Test
    public void isLowerCaseAndHyphensForSnakeCase() {
        assertThat(isLowerCaseAndHyphens("shipment_orders")).isFalse();
    }
    @Test
    public void isPathVariableForVariable() {
        assertThat(isPathVariable("{shipment_orders_id}")).isTrue();
    }
    @Test
    public void isPathVariableForJustCurlyBraces() {
        assertThat(isPathVariable("{}")).isTrue();
    }
    @Test
    public void isPathVariableForResourceName() {
        assertThat(isPathVariable("shipment-orders")).isFalse();
    }
    @Test
    public void isPathVariableForResourceNameAndStartingCurlyBrace() {
        assertThat(isPathVariable("{shipment-orders")).isFalse();
    }
    @Test
    public void isPathVariableForResourceNameTailingCurlyBrace() {
        assertThat(isPathVariable("shipment-orders}")).isFalse();
    }

    @Test
    public void validateEmptyPath(){
        assertThat(RULE.validate(TEST_SWAGGER)).isEmpty();
    }

    @Test
    public void validateNormalPath(){
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put(TEST_PATH_1, new Path());
        TEST_SWAGGER.paths(testData);
        assertThat(RULE.validate(TEST_SWAGGER)).isEmpty();
    }

    @Test
    public void validateMultipleNormalPaths(){
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put(TEST_PATH_1, new Path());
        testData.put(TEST_PATH_2, new Path());
        testData.put(TEST_PATH_3, new Path());
        TEST_SWAGGER.paths(testData);
        assertThat(RULE.validate(TEST_SWAGGER)).isEmpty();
    }

    @Test
    public void validateFalsePath(){
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put(WRONG_TEST_PATH_1, new Path());
        TEST_SWAGGER.paths(testData);
        List<Violation> result = RULE.validate(TEST_SWAGGER);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getPath().get()).isEqualTo(WRONG_TEST_PATH_1);
    }

    @Test
    public void validateMultipleFalsePaths(){
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put(WRONG_TEST_PATH_1, new Path());
        testData.put(TEST_PATH_2, new Path());
        testData.put(WRONG_TEST_PATH_2, new Path());
        TEST_SWAGGER.paths(testData);
        List<Violation> result = RULE.validate(TEST_SWAGGER);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(1).getPath().isPresent()).isTrue();
        assertThat(result.get(1).getPath().get()).isEqualTo(WRONG_TEST_PATH_1);
        assertThat(result.get(0).getPath().isPresent()).isTrue();
        assertThat(result.get(0).getPath().get()).isEqualTo(WRONG_TEST_PATH_2);
    }

}
