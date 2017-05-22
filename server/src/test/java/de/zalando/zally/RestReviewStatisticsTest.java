package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RestReviewStatisticsTest extends RestApiBaseTest {

    @Override
    protected String getUrl() {
        return "/review-statistics";
    }

    @Test
    public void shouldReturnEmptyReviewStatisticsList() {
        ResponseEntity<ReviewStatisticList> response = restTemplate.getForEntity(getUrl(), ReviewStatisticList.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getReviewStatistics()).isEmpty();
    }

    @Test
    public void shouldReturnAllReviewStatisticsFromLastWeekIfNoIntervalParametersAreSupplied() {
        LocalDate from = TestDateUtil.now().minusDays(7L).toLocalDate();

        List<ReviewStatistic> statistics = createRandomReviewStatisticInBetween(from, TestDateUtil.now().toLocalDate());

        ResponseEntity<ReviewStatisticList> response = restTemplate.getForEntity(getUrl(), ReviewStatisticList.class);

        assertThat(response.getBody().getReviewStatistics())
            .containsOnlyElementsOf(statistics)
            .hasSize(statistics.size());
    }

    @Test
    public void shouldReturnAllReviewStatisticsFromIntervalSpecifiedByFromParameterTilNow() {
        LocalDate from = TestDateUtil.now().minusDays(5L).toLocalDate();

        // this data should not be loaded later
        createRandomReviewStatisticInBetween(from.minusDays(10L), from.minusDays(5L));

        List<ReviewStatistic> statistics = createRandomReviewStatisticInBetween(from, TestDateUtil.now().toLocalDate());

        ResponseEntity<ReviewStatisticList> response = restTemplate.getForEntity(
            getUrl() + "?from=" + from.toString(), ReviewStatisticList.class);

        assertThat(response.getBody().getReviewStatistics())
            .containsOnlyElementsOf(statistics)
            .hasSize(statistics.size());
    }

    @Test
    public void shouldReturnAllReviewStatisticsFromIntervalSpecifiedByFromAndToParameters() {
        LocalDate from = TestDateUtil.now().minusDays(5L).toLocalDate();
        LocalDate to = TestDateUtil.yesterday().minusDays(1L).toLocalDate();

        List<ReviewStatistic> statistics = createRandomReviewStatisticInBetween(from, TestDateUtil.now().toLocalDate());

        ResponseEntity<ReviewStatisticList> response = restTemplate.getForEntity(
            getUrl() + "?from=" + from.toString() + "&to=" + to.toString(), ReviewStatisticList.class);

        assertThat(response.getBody().getReviewStatistics()).hasSize(statistics.size() - 1);
    }

    @Test
    public void shouldReturnBadRequestForFromInTheFuture() {
        expectBadRequestFor("?from=" + TestDateUtil.tomorrow().toLocalDate().toString());
    }

    @Test
    public void shouldReturnBadRequestForMalformedFromParameter() {
        expectBadRequestFor("?from=nodate");
    }

    @Test
    public void shouldReturnBadRequestForMalformedToParameter() {
        expectBadRequestFor("?to=nodate");
    }

    @Test
    public void shouldReturnBadRequestWhenToParameterIsProvidedWithoutFromParameter() {
        expectBadRequestFor("?to=2017-01-10");
    }

    private List<ReviewStatistic> createRandomReviewStatisticInBetween(LocalDate from, LocalDate to) {
        List<ReviewStatistic> statistics = new LinkedList<>();

        LocalDate currentDate = LocalDate.from(from);
        while (currentDate.isBefore(to)) {
            ReviewStatistic statistic = new ReviewStatistic(RandomStringUtils.randomAlphanumeric(10), RandomUtils.nextDouble());
            statistic.setDay(currentDate);

            statistics.add(statistic);
            currentDate = currentDate.plusDays(1L);
        }

        reviewStatisticRepository.save(statistics);
        return statistics;
    }

    private void expectBadRequestFor(String queryParameter) throws AssertionError {
        assertThat(requestStatistics(queryParameter).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<JsonNode> requestStatistics(String queryParameters) {
        return restTemplate.getForEntity(getUrl() + queryParameters, JsonNode.class);
    }
}
