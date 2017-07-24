package de.zalando.zally.apireview;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class RestCorsWithOAuthTest extends RestApiBaseTest {

    @Test
    public void shouldSupportCorsWhenOAuthIsEnabledOnAllResources() {
        assertThat(optionsRequest(API_VIOLATIONS_URL)).isEqualTo(HttpStatus.OK);
        assertThat(optionsRequest(SUPPORTED_RULES_URL)).isEqualTo(HttpStatus.OK);
        assertThat(optionsRequest(REVIEW_STATISTICS_URL)).isEqualTo(HttpStatus.OK);
    }

    private HttpStatus optionsRequest(String url) {
        return restTemplate.exchange(url, HttpMethod.OPTIONS, RequestEntity.EMPTY, String.class).getStatusCode();
    }
}
