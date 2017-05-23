package de.zalando.zally.apireview;

import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;

public interface ApiReviewRequestRepository extends CrudRepository<ApiReviewRequest, Long> {

    Collection<ApiReviewRequest> findByCreatedBetween(OffsetDateTime start, OffsetDateTime end);

    default Collection<ApiReviewRequest> findAllFromYesterday() {
        final OffsetDateTime yesterday = Instant.now().atOffset(ZoneOffset.UTC).minusDays(1L);
        return findByCreatedBetween(
            yesterday.toLocalDate().atStartOfDay().atOffset(ZoneOffset.UTC),
            yesterday.toLocalDate().atTime(23, 59, 59).atOffset(ZoneOffset.UTC));
    }
}
