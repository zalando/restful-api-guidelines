package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RulesValidatorTest {

    private static final Violation DUMMY_VIOLATION_1 = new Violation("dummy1", "dummy", ViolationType.MUST, "dummy");
    private static final Violation DUMMY_VIOLATION_2 = new Violation("dummy2", "dummy", ViolationType.MUST, "dummy");
    private static final Violation DUMMY_VIOLATION_3 = new Violation("dummy3", "dummy", ViolationType.MUST, "dummy");
    private static final Violation DUMMY_VIOLATION_PATH_A = new Violation("dummy3", "dummy", ViolationType.MUST, "dummy", "a");
    private static final Violation DUMMY_VIOLATION_PATH_Z = new Violation("dummy3", "dummy", ViolationType.MUST, "dummy", "z");

    private static class OneViolationRule implements Rule {

        @Override
        public List<Violation> validate(Swagger swagger) {
            return Arrays.asList(DUMMY_VIOLATION_1);
        }
    }

    private static class TwoViolationRule implements Rule {

        @Override
        public List<Violation> validate(Swagger swagger) {
            return Arrays.asList(DUMMY_VIOLATION_2, DUMMY_VIOLATION_3);
        }
    }

    private static class PathViolationRule implements Rule {

        @Override
        public List<Violation> validate(Swagger swagger) {
            return Arrays.asList(DUMMY_VIOLATION_PATH_Z, DUMMY_VIOLATION_PATH_A);
        }
    }

    @Test
    public void shouldReturnEmptyViolationsListWithoutRules() {
        RulesValidator validator = new RulesValidator(Collections.emptyList());
        assertThat(validator.validate(new Swagger())).isEmpty();
    }

    @Test
    public void shouldReturnOneViolation() {
        RulesValidator validator = new RulesValidator(Arrays.asList(new OneViolationRule()));
        assertThat(validator.validate(new Swagger())).containsOnly(DUMMY_VIOLATION_1);
    }

    @Test
    public void shouldCollectViolationsOfAllRules() {
        RulesValidator validator = new RulesValidator(Arrays.asList(new OneViolationRule(), new TwoViolationRule()));
        assertThat(validator.validate(new Swagger())).containsOnly(DUMMY_VIOLATION_1, DUMMY_VIOLATION_2, DUMMY_VIOLATION_3);
    }

    @Test
    public void shouldSortViolationsAlphabeticallyAndWithoutPathOnTop() {
        RulesValidator validator = new RulesValidator(Arrays.asList(new OneViolationRule(), new PathViolationRule()));
        assertThat(validator.validate(new Swagger())).containsExactly(DUMMY_VIOLATION_1, DUMMY_VIOLATION_PATH_A, DUMMY_VIOLATION_PATH_Z);
    }
}
