package de.zalando.zally;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ViolationTypeBinderTest {
    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseIllegalArgumentExceptionWhenTypeIsNotFound() {
        final ViolationTypeBinder typeBinder = new ViolationTypeBinder();
        typeBinder.setAsText("SOLUTION");
    }

    @Test
    public void shouldSetValueWhenTypeIsFound() {
        final ViolationTypeBinder typeBinder = new ViolationTypeBinder();
        final String[] allowedTypes = {"Must", "MUST", "must", "SHOULD", "COULD", "HINT"};

        for (String allowedType : allowedTypes) {
            ViolationType expectedType = ViolationType.valueOf(allowedType.toUpperCase());
            typeBinder.setAsText(allowedType);

            assertThat(typeBinder.getValue()).isEqualTo(expectedType);
        }
    }
}