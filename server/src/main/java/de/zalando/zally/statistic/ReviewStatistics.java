package de.zalando.zally.statistic;

import de.zalando.zally.apireview.ApiReview;
import de.zalando.zally.apireview.RuleViolation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewStatistics {

    private int totalReviews;
    private int successfulReviews;
    private int mustViolations;
    private int shouldViolations;
    private int mayViolations;
    private int hintViolations;
    private List<ApiReviewStatistic> reviews;
    private List<ViolationStatistic> violations;

    ReviewStatistics() {
    }

    ReviewStatistics(Collection<ApiReview> apiReviews) {
        totalReviews = apiReviews.size();
        successfulReviews = apiReviews.stream()
            .map(apiReview -> apiReview.isSuccessfulProcessed() ? 1 : 0)
            .mapToInt(Integer::intValue)
            .sum();
        mustViolations = apiReviews.stream().mapToInt(ApiReview::getMustViolations).sum();
        shouldViolations = apiReviews.stream().mapToInt(ApiReview::getShouldViolations).sum();
        mayViolations = apiReviews.stream().mapToInt(ApiReview::getMayViolations).sum();
        hintViolations = apiReviews.stream().mapToInt(ApiReview::getHintViolations).sum();
        reviews = apiReviews.stream().map(ApiReviewStatistic::new).collect(Collectors.toList());
        violations = apiReviews.stream()
            .flatMap(r -> r.getRuleViolations().stream())
            .collect(Collectors.groupingBy(RuleViolation::getName, Collectors.toList()))
            .entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .map(entry -> new ViolationStatistic(entry.getValue().get(0), entry.getValue().size()))
            .collect(Collectors.toList());
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public int getSuccessfulReviews() {
        return successfulReviews;
    }

    public void setSuccessfulReviews(int successfulReviews) {
        this.successfulReviews = successfulReviews;
    }

    public int getMustViolations() {
        return mustViolations;
    }

    public void setMustViolations(int mustViolations) {
        this.mustViolations = mustViolations;
    }

    public int getShouldViolations() {
        return shouldViolations;
    }

    public void setShouldViolations(int shouldViolations) {
        this.shouldViolations = shouldViolations;
    }

    public int getMayViolations() {
        return mayViolations;
    }

    public void setMayViolations(int mayViolations) {
        this.mayViolations = mayViolations;
    }

    public int getHintViolations() {
        return hintViolations;
    }

    public void setHintViolations(int hintViolations) {
        this.hintViolations = hintViolations;
    }

    public List<ApiReviewStatistic> getReviews() {
        return reviews;
    }

    public void setReviews(List<ApiReviewStatistic> reviews) {
        this.reviews = reviews;
    }

    public List<ViolationStatistic> getViolations() {
        return violations;
    }

    public void setViolations(List<ViolationStatistic> violations) {
        this.violations = violations;
    }
}
