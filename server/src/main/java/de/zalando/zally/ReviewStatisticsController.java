package de.zalando.zally;

import de.zalando.zally.exception.TimeParameterIsInTheFutureException;
import de.zalando.zally.exception.UnsufficientTimeIntervalParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@CrossOrigin
@RestController
public class ReviewStatisticsController {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewStatisticsController.class);

    private final ReviewStatisticRepository repository;

    @Autowired
    public ReviewStatisticsController(ReviewStatisticRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/review-statistics")
    public ResponseEntity<ReviewStatisticList> retrieveReviewStatistics(
        @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        if (from != null && from.isAfter(today())) {
            throw new TimeParameterIsInTheFutureException();
        }

        if (to != null && from == null) {
            throw new UnsufficientTimeIntervalParameterException();
        }

        if (to == null) {
            to = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate();
        }

        final List<ReviewStatistic> statistics = from != null
            ? repository.findByDayBetween(from, to)
            : repository.findAllFromLastWeek();

        LOG.info("Found {} statistics from {} to {}", statistics.size(), from, to);
        return ResponseEntity.ok(new ReviewStatisticList(statistics));
    }

    private LocalDate today() {
        return Instant.now().atOffset(ZoneOffset.UTC).toLocalDate();
    }
}
