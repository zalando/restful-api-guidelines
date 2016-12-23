package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AvoidTrailingSlashesRuleTest {
    private static final String TEST_PATH = "/api/test-api/";
    private static final String GOOD_PATH = "/api/test-api";

    @Test
    public void emptyAPI() {
        assertThat(new AvoidTrailingSlashesRule().validate(null)).isEmpty();
    }

    @Test
    public void withoutAnyViolations() {
        AvoidTrailingSlashesRule rule = new AvoidTrailingSlashesRule();
        Swagger testAPI = new Swagger();
        HashMap<String, Path> paths = new HashMap<>();
        paths.put(GOOD_PATH, null);
        testAPI.setPaths(paths);
        List<Violation> violations = rule.validate(testAPI);
        assertThat(violations).isEmpty();
    }

    @Test
    public void withViolations() {
        AvoidTrailingSlashesRule rule = new AvoidTrailingSlashesRule();
        Swagger testAPI = new Swagger();
        HashMap<String, Path> paths = new HashMap<>();
        paths.put(TEST_PATH, null);
        paths.put(GOOD_PATH, null);
        testAPI.setPaths(paths);
        List<Violation> violations = rule.validate(testAPI);
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.get(0).getPath().get()).isEqualTo(TEST_PATH);
    }
}
