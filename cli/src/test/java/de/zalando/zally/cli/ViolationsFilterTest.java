package de.zalando.zally.cli;

import static org.junit.Assert.assertEquals;

import de.zalando.zally.cli.domain.Violation;
import de.zalando.zally.cli.domain.ViolationType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class ViolationsFilterTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getMustViolationsReturnsOnlyMust() {
        List<Violation> fixtures = getFixtureViolations();
        Violation mustViolation = fixtures.get(0);

        ViolationsFilter violationsFilter = new ViolationsFilter(fixtures);

        List<Violation> result = violationsFilter.getViolations(ViolationType.MUST);
        assertEquals(1, result.size());
        assertEquals(mustViolation, result.get(0));
    }

    @Test
    public void getShouldViolationsReturnsOnlyShould() {
        List<Violation> fixtures = getFixtureViolations();
        Violation shouldViolation = fixtures.get(1);

        ViolationsFilter violationsFilter = new ViolationsFilter(fixtures);

        List<Violation> result = violationsFilter.getViolations(ViolationType.SHOULD);
        assertEquals(1, result.size());
        assertEquals(shouldViolation, result.get(0));
    }

    @Test
    public void getMayViolationsReturnsOnlyMay() {
        List<Violation> fixtures = getFixtureViolations();
        Violation mayViolations = fixtures.get(2);

        ViolationsFilter violationsFilter = new ViolationsFilter(fixtures);

        List<Violation> result = violationsFilter.getViolations(ViolationType.MAY);
        assertEquals(1, result.size());
        assertEquals(mayViolations, result.get(0));
    }


    @Test
    public void getHintViolationsReturnsOnlyHint() {
        List<Violation> fixtures = getFixtureViolations();
        Violation hintViolations = fixtures.get(3);

        ViolationsFilter violationsFilter = new ViolationsFilter(fixtures);

        List<Violation> result = violationsFilter.getViolations(ViolationType.HINT);
        assertEquals(1, result.size());
        assertEquals(hintViolations, result.get(0));
    }

    private List<Violation> getFixtureViolations() {
        final List<Violation> result = new ArrayList<>();

        for (ViolationType violationType : ViolationType.values()) {
            Violation violation = new Violation("Test " + violationType, "Test " + violationType + " description");
            violation.setViolationType(violationType);
            result.add(violation);
        }

        return result;
    }
}
