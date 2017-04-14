package de.zalando.zally.exception;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;


public class UnaccessibleResourceUrlExceptionTest {
    @Test
    public void shouldReturnParametersSpecifiedInConstructor() {
        final UnaccessibleResourceUrlException exception = new UnaccessibleResourceUrlException(
                "Test Message", HttpStatus.BAD_REQUEST);

        assertEquals("Test Message", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }
}
