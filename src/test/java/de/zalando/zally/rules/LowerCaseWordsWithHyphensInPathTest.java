package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LowerCaseWordsWithHyphensInPathTest extends LowerCaseWordsWithHyphensInPath {

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
        Swagger swagger = new Swagger();
        assertThat(validate(swagger)).isEmpty();
    }

    @Test
    public void validateNormalPath(){
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put("/shipment-order/{shipment_order_id}", new Path());
        Swagger swagger = new Swagger();
        swagger.paths(testData);
        assertThat(validate(swagger)).isEmpty();
    }

    @Test
    public void validateMultipleNormalPaths(){
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put("/shipment-order/{shipment_order_id}", new Path());
        testData.put("/partner-order/{partner_order_id}", new Path());
        testData.put("/partner-order/{partner_order_id}/partner-order/{partner_order_id}", new Path());
        Swagger swagger = new Swagger();
        swagger.paths(testData);
        assertThat(validate(swagger)).isEmpty();
    }

    @Test
    public void validateFalsePath(){
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put("/shipment_order/{shipment_order_id}", new Path());
        Swagger swagger = new Swagger();
        swagger.paths(testData);
        List<Violation> result = validate(swagger);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualToComparingFieldByField(new Violation(title, description, ViolationType.MUST, ruleLink, "/shipment_order/{shipment_order_id}"));
    }

    @Test
    public void validateMultipleFalsePaths(){
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put("/shipment_order/{shipment_order_id}", new Path());
        testData.put("/partner-order/{partner_order_id}", new Path());
        testData.put("/partner-order/{partner_order_id}/partner-order1/{partner_order_id}", new Path());
        Swagger swagger = new Swagger();
        swagger.paths(testData);
        List<Violation> result = validate(swagger);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(1)).isEqualToComparingFieldByField(new Violation(title, description, ViolationType.MUST, ruleLink, "/shipment_order/{shipment_order_id}"));
        assertThat(result.get(0)).isEqualToComparingFieldByField(new Violation(title, description, ViolationType.MUST, ruleLink, "/partner-order/{partner_order_id}/partner-order1/{partner_order_id}"));
    }

}
