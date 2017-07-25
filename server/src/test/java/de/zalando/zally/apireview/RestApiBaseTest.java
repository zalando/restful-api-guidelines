package de.zalando.zally.apireview;

import de.zalando.zally.Application;
import de.zalando.zally.dto.ApiDefinitionRequest;
import de.zalando.zally.dto.ApiDefinitionResponse;
import de.zalando.zally.statistic.ReviewStatistics;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {Application.class, RestApiTestConfiguration.class}
)
@ActiveProfiles("test")
public abstract class RestApiBaseTest {

    public static final String API_VIOLATIONS_URL = "/api-violations";
    public static final String REVIEW_STATISTICS_URL = "/review-statistics";
    public static final String SUPPORTED_RULES_URL = "/supported-rules";

    public static final String APPLICATION_PROBLEM_JSON = "application/problem+json";

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected ApiReviewRepository apiReviewRepository;

    @Before
    public void cleanDatabase() {
        apiReviewRepository.deleteAll();
    }

    protected final <T> ResponseEntity<T> sendApiDefinition(ApiDefinitionRequest request, Class<T> responseType) {
        return restTemplate.postForEntity(
                API_VIOLATIONS_URL, request, responseType
        );
    }

    protected final ApiDefinitionResponse sendApiDefinition(ApiDefinitionRequest request) {
        ResponseEntity<ApiDefinitionResponse> responseEntity = sendApiDefinition(request, ApiDefinitionResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return responseEntity.getBody();
    }


    protected final <T> ResponseEntity<T> getReviewStatistics(Object from, Object to, Class<T> responseType) {
        String url = fromPath(REVIEW_STATISTICS_URL)
                .queryParam("from", from)
                .queryParam("to", to)
                .build().encode().toUriString();

        return restTemplate.getForEntity(url, responseType);
    }

    protected final ReviewStatistics getReviewStatistics() {
        return getReviewStatistics(null, null);
    }

    protected final ReviewStatistics getReviewStatistics(LocalDate from, LocalDate to) {
        ResponseEntity<ReviewStatistics> responseEntity = getReviewStatistics(from, to, ReviewStatistics.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return responseEntity.getBody();
    }
}
