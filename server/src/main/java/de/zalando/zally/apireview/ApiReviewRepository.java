package de.zalando.zally.apireview;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;
import org.springframework.data.repository.CrudRepository;

public interface ApiReviewRepository extends CrudRepository<ApiReview, Long> {

    Collection<ApiReview> findByDay(LocalDate day);

    Collection<ApiReview> findByDayBetween(LocalDate from, LocalDate to);

    default Collection<ApiReview> findAllFromLastWeek() {
        final LocalDate today = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate();
        return findByDayBetween(today.minusDays(7L), today);
    }

    default Collection<ApiReview> findAllFromYesterday() {
        final LocalDate yesterday = Instant.now().atOffset(ZoneOffset.UTC).minusDays(1L).toLocalDate();
        return findByDay(yesterday);
    }
}
