package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.zalando.zally.rules.MediaTypesRule.isApplicationJsonOrProblemJson;
import static de.zalando.zally.rules.MediaTypesRule.isCustomMediaTypeWithVersioning;
import static org.assertj.core.api.Assertions.assertThat;

public class MediaTypesRuleTest {

    MediaTypesRule rule = new MediaTypesRule();
    Swagger testSwagger = new Swagger();
    Path testPath = new Path();
    Operation testOperation = new Operation();
    String testPathName = "/shipment-order/{shipment_order_id}";


    @Test
    public void isApplicationJsonOrProblemJsonForValidInput(){
        assertThat(isApplicationJsonOrProblemJson("application/json")).isTrue();
        assertThat(isApplicationJsonOrProblemJson("application/problem+json")).isTrue();
    }

    @Test
    public void isApplicationJsonOrProblemJsonForInvalidInput(){
        assertThat(isApplicationJsonOrProblemJson("application/vnd.api+json")).isFalse();
        assertThat(isApplicationJsonOrProblemJson("application/x.zalando.contract+json")).isFalse();
    }

    @Test
    public void isCustomMediaTypeWithVersioningForValidInput(){
        assertThat(isCustomMediaTypeWithVersioning("application/vnd.api+json;v=12")).isTrue();
        assertThat(isCustomMediaTypeWithVersioning("application/x.zalando.contract+json;v=34")).isTrue();
        assertThat(isCustomMediaTypeWithVersioning("application/vnd.api+json;version=123")).isTrue();
        assertThat(isCustomMediaTypeWithVersioning("application/x.zalando.contract+json;version=345")).isTrue();
    }

    @Test
    public void isCustomMediaTypeWithVersioningForInvalidInput(){
        assertThat(isCustomMediaTypeWithVersioning("application/vnd.api+json")).isFalse();
        assertThat(isCustomMediaTypeWithVersioning("application/x.zalando.contract+json")).isFalse();
        assertThat(isCustomMediaTypeWithVersioning("application/vnd.api+json;ver=1")).isFalse();
        assertThat(isCustomMediaTypeWithVersioning("application/x.zalando.contract+json;v:1")).isFalse();
        assertThat(isCustomMediaTypeWithVersioning("application/vnd.api+json;version=")).isFalse();
        assertThat(isCustomMediaTypeWithVersioning("application/x.zalando.contract+json;")).isFalse();
    }

    @Test
    public void isMediaTypeCorrectOrVersionForEmpty(){
     assertThat(rule.validate(testSwagger)).isEmpty();
    }

    @Test
    public void isMediaTypeCorrectOrVersionForValidSwagger(){
        testOperation.setProduces(Arrays.asList("application/json", "application/problem+json"));
        testOperation.setProduces(Arrays.asList("application/x.zalando.contract+json;v=123", "application/vnd.api+json;version=3"));
        testPath.set("get", testOperation);
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put(testPathName, testPath);
        testSwagger.paths(testData);
        assertThat(rule.validate(testSwagger)).isEmpty();
    }
    @Test
    public void isMediaTypeCorrectOrVersionForInvalidSwagger(){
        testOperation.setProduces(Arrays.asList("application/json", "application/vnd.api+json"));
        testPath.set("get", testOperation);
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put(testPathName, testPath);
        testSwagger.paths(testData);
        List<Violation> result = rule.validate(testSwagger);
        assertThat(result.size()).isEqualTo(1);
        assertThat((result.get(0).getPath().isPresent())).isTrue();
        assertThat(result.get(0).getPath().get()).isEqualTo("GET " + testPathName);
    }
    @Test
    public void isMediaTypeCorrectOrVersionForMultipleViolationsInSwagger(){
        testOperation.setProduces(Arrays.asList("application/json", "application/vnd.api+json"));
        testOperation.setConsumes(Arrays.asList("application/x.zalando.contract+json"));
        testPath.set("get", testOperation);
        testPath.set("put", testOperation);
        Map<String, Path> testData = new HashMap<String, Path>();
        testData.put(testPathName, testPath);
        testSwagger.paths(testData);
        List<String> paths = rule.validate(testSwagger).stream().map(v -> v.getPath().orElse("")).collect(Collectors.toList());
        assertThat(paths).hasSameElementsAs(Arrays.asList(
                "PUT /shipment-order/{shipment_order_id}",
                "PUT /shipment-order/{shipment_order_id}",
                "GET /shipment-order/{shipment_order_id}",
                "GET /shipment-order/{shipment_order_id}"
        ));
    }
}
