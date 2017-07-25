package de.zalando.zally.statistic;

import de.zalando.zally.apireview.ApiReview;
import de.zalando.zally.apireview.RestApiBaseTest;
import de.zalando.zally.dto.ApiDefinitionRequest;
import de.zalando.zally.dto.ViolationType;
import de.zalando.zally.rule.AvoidTrailingSlashesRule;
import de.zalando.zally.rule.Violation;
import de.zalando.zally.util.ErrorResponse;
import de.zalando.zally.util.TestDateUtil;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static de.zalando.zally.util.TestDateUtil.now;
import static de.zalando.zally.util.TestDateUtil.tomorrow;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class RestReviewStatisticsTest extends RestApiBaseTest {

    @Test
    public void shouldReturnEmptyReviewStatisticsList() {
        assertThat(getReviewStatistics().getReviews()).isEmpty();
    }

    @Test
    public void shouldReturnAllReviewStatisticsFromLastWeekIfNoIntervalParametersAreSupplied() {
        LocalDate from = now().minusDays(7L).toLocalDate();

        List<ApiReview> reviews = createRandomReviewsInBetween(from, now().toLocalDate());

        ReviewStatistics response = getReviewStatistics();

        assertThat(response.getReviews()).hasSize(reviews.size());
        assertThat(response.getViolations()).hasSize(1);
        assertThat(response.getViolations().get(0).getOccurrence()).isEqualTo(reviews.size());
    }

    @Test
    public void shouldReturnAllReviewStatisticsFromIntervalSpecifiedByFromParameterTilNow() {
        LocalDate from = now().minusDays(5L).toLocalDate();

        // this data should not be loaded later
        createRandomReviewsInBetween(from.minusDays(10L), from.minusDays(5L));

        List<ApiReview> reviews = createRandomReviewsInBetween(from, now().toLocalDate());

        ReviewStatistics response = getReviewStatistics(from, null);

        assertThat(response.getNumberOfEndpoints()).isEqualTo(reviews.size() * 2);
        assertThat(response.getMustViolations()).isEqualTo(reviews.size());
        assertThat(response.getTotalReviews()).isEqualTo(reviews.size());
        assertThat(response.getSuccessfulReviews()).isEqualTo(reviews.size());
        assertThat(response.getReviews()).hasSize(reviews.size());
        assertThat(response.getViolations()).hasSize(1);
    }

    @Test
    public void shouldReturnAllReviewStatisticsFromIntervalSpecifiedByFromAndToParameters() {
        LocalDate from = now().minusDays(5L).toLocalDate();
        LocalDate to = TestDateUtil.yesterday().minusDays(1L).toLocalDate();

        List<ApiReview> reviews = createRandomReviewsInBetween(from, now().toLocalDate());

        ReviewStatistics response = getReviewStatistics(from, to);
        assertThat(response.getReviews()).hasSize(reviews.size() - 1);
    }

    @Test
    public void shouldReturnBadRequestForFromInTheFuture() {
        assertBadRequestFor(tomorrow().toLocalDate(), null);
    }

    @Test
    public void shouldReturnBadRequestWhenToParameterIsProvidedWithoutFromParameter() {
        assertBadRequestFor(null, tomorrow().toLocalDate());
    }

    @Test
    public void shouldReturnBadRequestForMalformedFromParameter() {
        assertBadRequestFor("nodate", null);
    }

    @Test
    public void shouldReturnBadRequestForMalformedToParameter() {
        assertBadRequestFor(null, "nodate");
    }

    @Test
    public void shouldReturnApiName() {
        int reviewsCount = 7;
        createRandomReviewsInBetween(now().minusDays(reviewsCount).toLocalDate(), now().toLocalDate());
        ReviewStatistics response = getReviewStatistics();
        assertThat(response.getReviews()).hasSize(reviewsCount);
        assertThat(response.getReviews().get(0).getApi()).isEqualTo("My API");
    }

    private List<ApiReview> createRandomReviewsInBetween(LocalDate from, LocalDate to) {
        List<ApiReview> reviews = new LinkedList<>();

        LocalDate currentDate = LocalDate.from(from);
        while (currentDate.isBefore(to)) {
            ApiReview review = new ApiReview(new ApiDefinitionRequest(), "dummyApiDefinition", createRandomViolations());
            review.setDay(currentDate);
            review.setName("My API");
            review.setNumberOfEndpoints(2);

            reviews.add(review);
            currentDate = currentDate.plusDays(1L);
        }

        apiReviewRepository.save(reviews);
        return reviews;
    }

    private List<Violation> createRandomViolations() {
        return Arrays.asList(new Violation(new AvoidTrailingSlashesRule(), "", "", ViolationType.MUST, "", Arrays.asList("path")));
    }

    private void assertBadRequestFor(Object from, Object to) {
        ResponseEntity<ErrorResponse> response = getReviewStatistics(from, to, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getHeaders().getContentType().toString()).isEqualTo(APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody().getTitle()).isEqualTo(BAD_REQUEST.getReasonPhrase());
        assertThat(response.getBody().getStatus()).isNotEmpty();
        assertThat(response.getBody().getDetail()).isNotEmpty();
    }
}
