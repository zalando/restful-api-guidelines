package de.zalando.zally.statistic;

import de.zalando.zally.apireview.ApiReview;
import de.zalando.zally.apireview.ApiReviewRepository;
import de.zalando.zally.exception.TimeParameterIsInTheFutureException;
import de.zalando.zally.exception.UnsufficientTimeIntervalParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;

@CrossOrigin
@RestController
public class ReviewStatisticsController {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewStatisticsController.class);

    private final ApiReviewRepository apiReviewRepository;

    @Autowired
    public ReviewStatisticsController(ApiReviewRepository apiReviewRepository) {
        this.apiReviewRepository = apiReviewRepository;
    }

    @ResponseBody
    @GetMapping("/review-statistics")
    public ReviewStatistics retrieveReviewStatistics(
        @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        if (from != null && from.isAfter(today())) {
            throw new TimeParameterIsInTheFutureException();
        }

        if (to != null && from == null) {
            throw new UnsufficientTimeIntervalParameterException();
        }

        final Collection<ApiReview> apiReviews = from != null
            ? apiReviewRepository.findByDayBetween(from, to != null ? to : today())
            : apiReviewRepository.findAllFromLastWeek();

        LOG.info("Found {} api reviews from {} to {}", apiReviews.size(), from, to);
        return new ReviewStatistics(apiReviews);
    }

    private LocalDate today() {
        return Instant.now().atOffset(ZoneOffset.UTC).toLocalDate();
    }
}
