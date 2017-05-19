package de.zalando.zally;

import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

interface ApiReviewRequestRepository extends CrudRepository<ApiReviewRequest, Long> {

    Collection<ApiReviewRequest> findByCreatedBetween(OffsetDateTime start, OffsetDateTime end);

    default Collection<ApiReviewRequest> findAllFromYesterday() {
        final OffsetDateTime yesterday = Instant.now().minus(1L, ChronoUnit.DAYS).atOffset(ZoneOffset.UTC);
        return findByCreatedBetween(
            yesterday.toLocalDate().atStartOfDay().atOffset(ZoneOffset.UTC),
            yesterday.toLocalDate().atTime(23, 59, 59).atOffset(ZoneOffset.UTC));
    }
}
