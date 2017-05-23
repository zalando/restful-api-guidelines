package de.zalando.zally;

import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public interface ReviewStatisticRepository extends CrudRepository<ReviewStatistic, String> {

    List<ReviewStatistic> findByDayBetween(LocalDate from, LocalDate to);

    default List<ReviewStatistic> findAllFromLastWeek() {
        final LocalDate today = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate();
        return findByDayBetween(today.minusDays(7L), today);
    }
}
