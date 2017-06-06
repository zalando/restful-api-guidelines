package de.zalando.zally.statistic;

import de.zalando.zally.apireview.ApiReview;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewStatistics {

    private int reviews;
    private int apisWithoutViolations;
    private int apisWithMustViolations;
    private int apisWithShouldViolations;
    private int apisWithMayViolations;
    private int apisWithHintViolations;
    private List<ApiReviewStatistic> apis;

    ReviewStatistics() {
    }

    ReviewStatistics(Collection<ApiReview> apiReviews) {
        reviews = apiReviews.size();
        apisWithoutViolations = (int) apiReviews.stream().filter(review -> review.getRuleViolations().isEmpty()).count();
        apisWithMustViolations = (int) apiReviews.stream().filter(review -> review.getMustViolations() > 0).count();
        apisWithShouldViolations = (int) apiReviews.stream().filter(review -> review.getShouldViolations() > 0).count();
        apisWithMayViolations = (int) apiReviews.stream().filter(review -> review.getMayViolations() > 0).count();
        apisWithHintViolations = (int) apiReviews.stream().filter(review -> review.getHintViolations() > 0).count();
        apis = apiReviews.stream().map(ApiReviewStatistic::new).collect(Collectors.toList());
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public int getApisWithoutViolations() {
        return apisWithoutViolations;
    }

    public void setApisWithoutViolations(int apisWithoutViolations) {
        this.apisWithoutViolations = apisWithoutViolations;
    }

    public int getApisWithMustViolations() {
        return apisWithMustViolations;
    }

    public void setApisWithMustViolations(int apisWithMustViolations) {
        this.apisWithMustViolations = apisWithMustViolations;
    }

    public int getApisWithShouldViolations() {
        return apisWithShouldViolations;
    }

    public void setApisWithShouldViolations(int apisWithShouldViolations) {
        this.apisWithShouldViolations = apisWithShouldViolations;
    }

    public int getApisWithMayViolations() {
        return apisWithMayViolations;
    }

    public void setApisWithMayViolations(int apisWithMayViolations) {
        this.apisWithMayViolations = apisWithMayViolations;
    }

    public int getApisWithHintViolations() {
        return apisWithHintViolations;
    }

    public void setApisWithHintViolations(int apisWithHintViolations) {
        this.apisWithHintViolations = apisWithHintViolations;
    }

    public List<ApiReviewStatistic> getApis() {
        return apis;
    }

    public void setApis(List<ApiReviewStatistic> apis) {
        this.apis = apis;
    }
}
