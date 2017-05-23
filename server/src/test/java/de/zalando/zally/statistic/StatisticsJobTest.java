package de.zalando.zally.statistic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import de.zalando.zally.apireview.ApiReviewRequest;
import de.zalando.zally.apireview.ApiReviewRequestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static de.zalando.zally.util.TestDateUtil.yesterday;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class StatisticsJobTest {

    @Autowired
    private StatisticsJob statisticsJob;

    @Autowired
    private ApiReviewRequestRepository reviewRequestRepository;

    @Autowired
    private ReviewStatisticRepository statisticRepository;

    @Before
    public void cleanDatabase() {
        statisticRepository.deleteAll();
        reviewRequestRepository.deleteAll();
    }

    @Test
    public void shouldReportOneApiReviewRequestAtAll() {
        reviewRequestRepository.save(createReviewRequest());

        statisticsJob.gatherStatistics();

        assertStatisticWasReportedExactlyOnce("api-reviews.all");
    }

    @Test
    public void shouldReportOneViolationAtAll() {
        reviewRequestRepository.save(createReviewRequest());

        statisticsJob.gatherStatistics();

        assertStatisticWasReportedExactlyOnce("api-reviews.violations.all");
    }

    @Test
    public void shouldReportOneMustViolation() {
        reviewRequestRepository.save(createReviewRequest());

        statisticsJob.gatherStatistics();

        assertStatisticWasReportedExactlyOnce("api-reviews.violations.type.must");
    }

    @Test
    public void shouldReportInvalidSchemaRuleViolation() {
        reviewRequestRepository.save(createReviewRequest());

        statisticsJob.gatherStatistics();

        assertStatisticWasReportedExactlyOnce("api-reviews.violations.rule.invalidapischemarule");
    }

    private void assertStatisticWasReportedExactlyOnce(String name) {
        List<ReviewStatistic> allStatistics = Lists.newArrayList(statisticRepository.findAll()).stream()
            .filter(statistic -> statistic.getName().equals(name))
            .collect(Collectors.toList());

        assertThat(allStatistics).hasSize(1);
        assertThat(allStatistics.get(0).getName()).isEqualTo(name);
        assertThat(allStatistics.get(0).getValue()).isEqualTo(1);
    }

    private ApiReviewRequest createReviewRequest() {
        return createReviewRequest(new ObjectMapper().createObjectNode());
    }

    private ApiReviewRequest createReviewRequest(JsonNode jsonPayload) {
        ApiReviewRequest reviewRequest = new ApiReviewRequest(jsonPayload, jsonPayload.toString());
        reviewRequest.setCreated(yesterday());
        return reviewRequest;
    }
}
