package de.zalando.zally.util;

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
        return now().minusDays(1L);
    }

    public static OffsetDateTime tomorrow() {
        return now().plusDays(1L);
    }
}
