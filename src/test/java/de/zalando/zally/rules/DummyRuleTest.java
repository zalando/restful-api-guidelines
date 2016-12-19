package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.Swagger;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Just an example.
 */
public class DummyRuleTest {

    private static class DummyRule implements Rule {

        @Override
        public List<Violation> validate(Swagger swagger) {
            return new ArrayList<>();
        }
    }

    @Test
    public void dummyRule() {
        assertThat(new DummyRule().validate(null)).isEmpty();
    }
}
