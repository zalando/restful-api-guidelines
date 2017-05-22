package de.zalando.zally;

import java.util.LinkedList;
import java.util.List;

public class ReviewStatisticList {

    private final List<ReviewStatistic> reviewStatistics;

    protected ReviewStatisticList() {
        this(new LinkedList<>());
    }

    public ReviewStatisticList(List<ReviewStatistic> reviewStatistics) {
        this.reviewStatistics = reviewStatistics;
    }

    public List<ReviewStatistic> getReviewStatistics() {
        return reviewStatistics;
    }
}
