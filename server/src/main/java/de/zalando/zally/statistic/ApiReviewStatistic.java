package de.zalando.zally.statistic;

import de.zalando.zally.apireview.ApiReview;
import de.zalando.zally.apireview.RuleViolation;

import java.util.LinkedList;
import java.util.List;

public class ApiReviewStatistic {

    private String api;
    private boolean successful;
    private int numberOfEndpoints;
    private List<RuleViolation> violations;

    ApiReviewStatistic() {
    }

    ApiReviewStatistic(ApiReview apiReview) {
        api = apiReview.getName();
        successful = apiReview.isSuccessfulProcessed();
        numberOfEndpoints = apiReview.getNumberOfEndpoints();
        violations = new LinkedList<>(apiReview.getRuleViolations());
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public int getNumberOfEndpoints() {
        return numberOfEndpoints;
    }

    public void setNumberOfEndpoints(int numberOfEndpoints) {
        this.numberOfEndpoints = numberOfEndpoints;
    }

    public List<RuleViolation> getViolations() {
        return violations;
    }

    public void setViolations(List<RuleViolation> violations) {
        this.violations = violations;
    }
}
