package de.zalando.zally.statistic;

import de.zalando.zally.apireview.ApiReviewRequest;
import de.zalando.zally.apireview.ApiReviewRequestRepository;
import de.zalando.zally.rule.RulesValidator;
import de.zalando.zally.violation.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StatisticsJob {

    private static final Logger LOG = LoggerFactory.getLogger(StatisticsJob.class);

    private static final long DAILY_IN_MS = 24 * 60 * 60 * 1000L;

    private final ApiReviewRequestRepository repository;
    private final ReviewStatisticRepository statisticRepository;
    private final RulesValidator rulesValidator;

    @Autowired
    public StatisticsJob(ApiReviewRequestRepository repository,
                         ReviewStatisticRepository statisticRepository,
                         RulesValidator rulesValidator) {
        this.repository = repository;
        this.statisticRepository = statisticRepository;
        this.rulesValidator = rulesValidator;
    }

    @Scheduled(initialDelay = DAILY_IN_MS, fixedRate = DAILY_IN_MS)
    public void gatherStatistics() {
        LOG.info("Started gathering statistics...");
        final Collection<ApiReviewRequest> yesterdayReviewRequests = repository.findAllFromYesterday();
        final List<Violation> yesterdayViolations = yesterdayReviewRequests.stream()
            .flatMap(reviewRequest -> rulesValidator.validate(reviewRequest.getApiDefinition()).stream())
            .collect(Collectors.toList());

        reportApiReviewRequests(yesterdayReviewRequests);
        reportViolationsCount(yesterdayViolations);
        reportViolationTypes(yesterdayViolations);
        reportViolatedRules(yesterdayViolations);

        LOG.info("Finished gathering statistics.");
    }

    private void reportApiReviewRequests(Collection<ApiReviewRequest> reviewRequests) {
        statisticRepository.save(new ReviewStatistic("api-reviews.all", reviewRequests.size()));
    }

    private void reportViolationsCount(List<Violation> violations) {
        statisticRepository.save(new ReviewStatistic("api-reviews.violations.all", violations.size()));
    }

    private void reportViolationTypes(Collection<Violation> violations) {
        violations.stream()
            .collect(Collectors.groupingBy(Violation::getViolationType))
            .forEach((violationType, v) ->
                statisticRepository.save(new ReviewStatistic(
                    "api-reviews.violations.type." + violationType.name().toLowerCase(), v.size()))
            );
    }

    private void reportViolatedRules(List<Violation> violations) {
        violations.stream()
            .collect(Collectors.groupingBy(Violation::getRule))
            .forEach((violationType, v) ->
                statisticRepository.save(new ReviewStatistic(
                    "api-reviews.violations.rule." + toLowercaseWithDashes(violationType.getName()), v.size()))
            );
    }

    private String toLowercaseWithDashes(String str) {
        return str.toLowerCase().replace(' ', '-');
    }
}
