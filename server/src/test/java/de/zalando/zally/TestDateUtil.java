package de.zalando.zally;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public final class TestDateUtil {

    private TestDateUtil() {
    }

    public static OffsetDateTime yesterday() {
        return Instant.now().minus(1L, ChronoUnit.DAYS).atOffset(ZoneOffset.UTC);
    }
}
