package de.zalando.zally;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class TestDateUtil {

    private TestDateUtil() {
    }

    public static OffsetDateTime now() {
        return Instant.now().atOffset(ZoneOffset.UTC);
    }

    public static OffsetDateTime yesterday() {
        return Instant.now().atOffset(ZoneOffset.UTC).minusDays(1L);
    }

    public static OffsetDateTime tomorrow() {
        return Instant.now().atOffset(ZoneOffset.UTC).plusDays(1L);
    }
}
