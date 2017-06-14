package de.zalando.zally.statistic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.zalando.zally.apireview.ApiReview;
import de.zalando.zally.apireview.RestApiBaseTest;
import de.zalando.zally.rule.InvalidApiSchemaRule;
import de.zalando.zally.util.TestDateUtil;
import de.zalando.zally.violation.Violation;
import de.zalando.zally.violation.ViolationType;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RestReviewStatisticsTest extends RestApiBaseTest {

    private final JsonNode emptyJsonPayload = new ObjectMapper().createObjectNode();

    @Override
    protected String getUrl() {
        return "/review-statistics";
    }

    @Test
    public void shouldReturnEmptyReviewStatisticsList() {
        ResponseEntity<ReviewStatistics> response = restTemplate.getForEntity(getUrl(), ReviewStatistics.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getReviews()).isEmpty();
    }

    @Test
    public void shouldFormatJsonFieldProperlyWithSnakeCase() {
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(getUrl(), JsonNode.class);

        assertThat(response.getBody().has("must_violations")).isTrue();
        assertThat(response.getBody().has("total_reviews")).isTrue();
        assertThat(response.getBody().has("successful_reviews")).isTrue();
    }

    @Test
    public void shouldReturnAllReviewStatisticsFromLastWeekIfNoIntervalParametersAreSupplied() {
        LocalDate from = TestDateUtil.now().minusDays(7L).toLocalDate();

        List<ApiReview> reviews = createRandomReviewsInBetween(from, TestDateUtil.now().toLocalDate());

        ResponseEntity<ReviewStatistics> response = restTemplate.getForEntity(getUrl(), ReviewStatistics.class);

        assertThat(response.getBody().getReviews()).hasSize(reviews.size());
        assertThat(response.getBody().getViolations()).hasSize(1);
        assertThat(response.getBody().getViolations().get(0).getOccurrence()).isEqualTo(reviews.size());
    }

    @Test
    public void shouldReturnAllReviewStatisticsFromIntervalSpecifiedByFromParameterTilNow() {
        LocalDate from = TestDateUtil.now().minusDays(5L).toLocalDate();

        // this data should not be loaded later
        createRandomReviewsInBetween(from.minusDays(10L), from.minusDays(5L));

        List<ApiReview> reviews = createRandomReviewsInBetween(from, TestDateUtil.now().toLocalDate());

        ResponseEntity<ReviewStatistics> response = restTemplate.getForEntity(
            getUrl() + "?from=" + from.toString(), ReviewStatistics.class);

        assertThat(response.getBody().getMustViolations()).isEqualTo(reviews.size());
        assertThat(response.getBody().getTotalReviews()).isEqualTo(reviews.size());
        assertThat(response.getBody().getSuccessfulReviews()).isEqualTo(reviews.size());
        assertThat(response.getBody().getReviews()).hasSize(reviews.size());
        assertThat(response.getBody().getViolations()).hasSize(1);
    }

    @Test
    public void shouldReturnAllReviewStatisticsFromIntervalSpecifiedByFromAndToParameters() {
        LocalDate from = TestDateUtil.now().minusDays(5L).toLocalDate();
        LocalDate to = TestDateUtil.yesterday().minusDays(1L).toLocalDate();

        List<ApiReview> reviews = createRandomReviewsInBetween(from, TestDateUtil.now().toLocalDate());

        ResponseEntity<ReviewStatistics> response = restTemplate.getForEntity(
            getUrl() + "?from=" + from.toString() + "&to=" + to.toString(), ReviewStatistics.class);

        assertThat(response.getBody().getReviews()).hasSize(reviews.size() - 1);
    }

    @Test
    public void shouldReturnBadRequestForFromInTheFuture() {
        assertBadRequestFor("?from=" + TestDateUtil.tomorrow().toLocalDate().toString());
    }

    @Test
    public void shouldReturnBadRequestForMalformedFromParameter() {
        assertBadRequestFor("?from=nodate");
    }

    @Test
    public void shouldReturnBadRequestForMalformedToParameter() {
        assertBadRequestFor("?to=nodate");
    }

    @Test
    public void shouldReturnBadRequestWhenToParameterIsProvidedWithoutFromParameter() {
        assertBadRequestFor("?to=2017-01-10");
    }

    @Test
    public void shouldReturnApiName() {
        createRandomReviewsInBetween(TestDateUtil.now().minusDays(7L).toLocalDate(), TestDateUtil.now().toLocalDate());
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(getUrl(), JsonNode.class);
        assertThat(response.getBody().toString()).contains("My API");
    }

    private List<ApiReview> createRandomReviewsInBetween(LocalDate from, LocalDate to) {
        List<ApiReview> reviews = new LinkedList<>();

        LocalDate currentDate = LocalDate.from(from);
        while (currentDate.isBefore(to)) {
            ApiReview review = new ApiReview(emptyJsonPayload, "dummyApiDefinition", createRandomViolations());
            review.setDay(currentDate);
            review.setName("My API");

            reviews.add(review);
            currentDate = currentDate.plusDays(1L);
        }

        apiReviewRepository.save(reviews);
        return reviews;
    }

    private List<Violation> createRandomViolations() {
        return Arrays.asList(new Violation(new InvalidApiSchemaRule(), "", "", ViolationType.MUST, "", Arrays.asList("path")));
    }

    private void assertBadRequestFor(String queryParameter) throws AssertionError {
        assertThat(requestStatistics(queryParameter).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<JsonNode> requestStatistics(String queryParameters) {
        return restTemplate.getForEntity(getUrl() + queryParameters, JsonNode.class);
    }
}
